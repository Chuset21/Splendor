package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.card.CardLevel;
import hexanome.fourteen.server.model.board.card.StandardCard;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Game Board.
 * We might have to include a different class, GameBoard, with no functionality,
 * and only the necessary information to send to the clients (We'll see).
 * If so this might be true for more than just this class.
 */
public final class GameBoard {

  private static final int WINNING_POINTS = 15;

  /**
   * The current player's turn.
   */
  private int playerTurn;
  /**
   * The turn indicating what player started the game.
   */
  private final int startingPlayerTurn;
  /**
   * A map to convert player turn to the actual player.
   */
  private final Map<Integer, Player> playerTurnMap;
  private final Set<Noble> availableNobles;
  /**
   * The gems in the bank.
   */
  private final Gems availableGems;
  /**
   * The cards on the board, including the one's that are not face up.
   * I'm interpreting it as each list being a list of a certain
   * level of cards, with the first x amount in each list being the cards
   * that are face up.
   */
  private final Set<List<Card>> cards;
  private final Set<Expansion> expansions;
  /**
   * The current leading player.
   */
  private Player leadingPlayer;
  private final Set<Player> players;
  private String gameid;
  private boolean lastRound;
  private final String creator;

  /**
   * Constructor.
   *
   * @param expansions      The set of expansions
   * @param players         The players
   * @param gameid          The game ID
   * @param creator         The creator of the game
   */
  public GameBoard(Set<Expansion> expansions, Set<Player> players, String gameid, String creator) {
    playerTurnMap = new HashMap<>();
    int count = 0;
    for (Player player : players) {
      playerTurnMap.put(count++, player);
    }
    playerTurn = new Random().nextInt(0, count);
    startingPlayerTurn = playerTurn;
    lastRound = false;
    leadingPlayer = playerTurnMap.get(0);

    availableGems = new Gems();
    final int gemAmount = count == 2 ? 4 : count == 3 ? 5 : 7;
    for (GemColor gemColor : GemColor.values()) {
      availableGems.put(gemColor, gemAmount);
    }
    availableGems.put(GemColor.GOLD, 5);

    this.availableNobles = createNobles(count + 1);
    this.cards = createDecks(expansions);
    this.expansions = expansions;
    this.players = players;
    this.gameid = gameid;
    this.creator = creator;
  }

  /**
   * Creates the decks for the game.
   *
   * @param expansions Expansions being played.
   * @return All the decks within the game.
   */
  private static Set<List<Card>> createDecks(Set<Expansion> expansions) {
    Set<List<Card>> decks = new HashSet<>();

    List<Card> levelOne = new ArrayList<>();
    List<Card> levelTwo = new ArrayList<>();
    List<Card> levelThree = new ArrayList<>();

    List<Card> orientLevelOne = new ArrayList<>();
    List<Card> orientLevelTwo = new ArrayList<>();
    List<Card> orientLevelThree = new ArrayList<>();
    // Fill list with final cards
    try {
      // Get CardData.csv file
      BufferedReader br = new BufferedReader(new InputStreamReader(
          Objects.requireNonNull(GameBoard.class.getResourceAsStream("CardData.csv"))));

      // Skip header of the CSV file
      br.readLine();

      String curLine;
      // Read through lines of file and get card data
      while ((curLine = br.readLine()) != null) {

        // Use comma as a delimiter
        String[] cardData = curLine.split(",");

        // Get the Cost
        Gems cost = new Gems();
        final int greenCost = Integer.parseInt(cardData[0]);
        cost.computeIfAbsent(GemColor.GREEN, k -> greenCost > 0 ? greenCost : null);

        final int whiteCost = Integer.parseInt(cardData[1]);
        cost.computeIfAbsent(GemColor.WHITE, k -> whiteCost > 0 ? whiteCost : null);

        final int blueCost = Integer.parseInt(cardData[2]);
        cost.computeIfAbsent(GemColor.BLUE, k -> blueCost > 0 ? blueCost : null);

        final int blackCost = Integer.parseInt(cardData[3]);
        cost.computeIfAbsent(GemColor.BLACK, k -> blackCost > 0 ? blackCost : null);

        final int redCost = Integer.parseInt(cardData[4]);
        cost.computeIfAbsent(GemColor.RED, k -> redCost > 0 ? redCost : null);

        // Get the level and expansion
        CardLevel level = CardLevel.valueOf(cardData[7]);
        Expansion expansion = Expansion.valueOf(cardData[6]);

        // Create card
        Card c = new StandardCard(Integer.parseInt(cardData[8]), cost, level, expansion,
            GemColor.valueOf(cardData[5]));

        // Add the card to respective list
        if (expansion == Expansion.STANDARD) {
          switch (level) {
            case ONE -> levelOne.add(c);
            case TWO -> levelTwo.add(c);
            case THREE -> levelThree.add(c);
            default -> throw new IllegalStateException("Unexpected value: " + level);
          }
        } else if (expansions.contains(Expansion.ORIENT) && expansion == Expansion.ORIENT) {
          switch (level) {
            case ONE -> orientLevelOne.add(c);
            case TWO -> orientLevelTwo.add(c);
            case THREE -> orientLevelThree.add(c);
            default -> throw new IllegalStateException("Unexpected value: " + level);
          }
        }
      }

      decks.add(levelOne);
      decks.add(levelTwo);
      decks.add(levelThree);
      if (expansions.contains(Expansion.ORIENT)) {
        decks.add(orientLevelOne);
        decks.add(orientLevelTwo);
        decks.add(orientLevelThree);
      }

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }

    return decks;
  }

  private static Set<Noble> createNobles(int numberOfNobles) {
    final List<Noble> nobles = new ArrayList<>();

    try {
      final BufferedReader br = new BufferedReader(new InputStreamReader(
          Objects.requireNonNull(GameBoard.class.getResourceAsStream("NobleData.csv"))));
      br.readLine();

      String curLine;
      while ((curLine = br.readLine()) != null) {
        final String[] nobleData = curLine.split(",");

        final Gems cost = new Gems();
        final int greenCost = Integer.parseInt(nobleData[0]);
        cost.computeIfAbsent(GemColor.GREEN, k -> greenCost > 0 ? greenCost : null);

        final int whiteCost = Integer.parseInt(nobleData[1]);
        cost.computeIfAbsent(GemColor.WHITE, k -> whiteCost > 0 ? whiteCost : null);

        final int blueCost = Integer.parseInt(nobleData[2]);
        cost.computeIfAbsent(GemColor.BLUE, k -> blueCost > 0 ? blueCost : null);

        final int blackCost = Integer.parseInt(nobleData[3]);
        cost.computeIfAbsent(GemColor.BLACK, k -> blackCost > 0 ? blackCost : null);

        final int redCost = Integer.parseInt(nobleData[4]);
        cost.computeIfAbsent(GemColor.RED, k -> redCost > 0 ? redCost : null);

        final int prestigePoints = Integer.parseInt(nobleData[5]);
        nobles.add(new Noble(prestigePoints, cost));
      }
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    Collections.shuffle(nobles);

    return new HashSet<>(nobles.subList(0, numberOfNobles));
  }

  /**
   * A Getter for the Player's Turn.
   *
   * @return The Player's Turn
   */
  public int playerTurn() {
    return playerTurn;
  }

  /**
   * A Getter for the Player's Turn Map.
   *
   * @return The Player's Turn Map
   */
  public Map<Integer, Player> playerTurnMap() {
    return playerTurnMap;
  }

  /**
   * A Getter for the Available Nobles.
   *
   * @return The Available Nobles
   */
  public Set<Noble> availableNobles() {
    return availableNobles;
  }

  /**
   * A Getter for the Available Gems.
   *
   * @return The Available Gems
   */
  public Gems availableGems() {
    return availableGems;
  }

  /**
   * A Getter for the Available Cards.
   *
   * @return The Available Cards
   */
  public Set<List<Card>> cards() {
    return cards;
  }

  /**
   * A Getter for the current chosen Expansions.
   *
   * @return The chosen Expansions
   */
  public Set<Expansion> expansions() {
    return expansions;
  }

  /**
   * A Getter for the current Leading Player.
   *
   * @return The Leading Player
   */
  public Player leadingPlayer() {
    return leadingPlayer;
  }

  /**
   * A Getter for the all the Players in the Game.
   *
   * @return The Players in the Game
   */
  public Set<Player> players() {
    return players;
  }

  /**
   * A Getter for the Game ID.
   *
   * @return The Game ID
   */
  public String gameid() {
    return gameid;
  }

  public void setGameid(String gameid) {
    this.gameid = gameid;
  }

  /**
   * A Getter for the Creator of the Game.
   *
   * @return The Creator of the Game
   */
  public String creator() {
    return creator;
  }

  /**
   * Compute the leading player.
   */
  public void computeLeadingPlayer() {
    final int leadingCount = leadingPlayer.hand().prestigePoints();
    final List<Player> playersWithHigherCount =
        players.stream().filter(p -> p.hand().prestigePoints() > leadingCount).toList();

    if (playersWithHigherCount.isEmpty()) {
      final List<Player> playersWithEqualCount =
          players.stream().filter(p -> p.hand().prestigePoints() == leadingCount).toList();
      if (playersWithEqualCount.size() > 1) {
        leadingPlayer = playersWithEqualCount.stream()
            .min(Comparator.comparingInt(p -> p.hand().purchasedCards().size()))
            .orElse(leadingPlayer);
      }
    } else {
      leadingPlayer = playersWithHigherCount.get(0);
    }
  }

  /**
   * Go to the next turn.
   *
   * @return true if it's just now the last round of the game, false otherwise.
   */
  public boolean nextTurn() {
    boolean wasLastTurn = lastRound;
    if (!lastRound) {
      computeLeadingPlayer();
      if (leadingPlayer.hand().prestigePoints() >= WINNING_POINTS) {
        lastRound = true;
      }
    }
    playerTurn = (playerTurn + 1) % players.size();
    return wasLastTurn == lastRound;
  }

  /**
   * Returns whether the game is over or not.
   *
   * @return true if the game is over, false otherwise.
   */
  public boolean isGameOver() {
    return lastRound && playerTurn == startingPlayerTurn;
  }

  /**
   * Compute the claimable nobles.
   *
   * @param hand the player's hand
   * @return the set of claimable nobles by this player.
   */
  public Set<Noble> computeClaimableNobles(Hand hand) {
    final Set<Noble> nobles = new HashSet<>(availableNobles);
    nobles.addAll(hand.reservedNobles());
    return nobles.stream()
        .filter(n -> hand.gemDiscounts().hasEnoughGems(n.cost()))
        .collect(Collectors.toSet());
  }
}
