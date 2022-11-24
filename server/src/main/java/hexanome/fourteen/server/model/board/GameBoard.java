package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Game Board
 * We might have to include a different class, GameBoard, with no functionality,
 * and only the necessary information to send to the clients (We'll see).
 * If so this might be true for more than just this class.
 */
public final class GameBoard {
  /**
   * The current player's turn
   */
  private final int playerTurn;
  /**
   * A map to convert player turn to the actual player
   */
  private final Map<Integer, Player> playerTurnMap;
  private final Set<Noble> availableNobles;
  /**
   * The gems in the bank
   */
  private final Gems availableGems;
  private final List<List<Card>> cards;
  private final Set<Expansion> expansions;
  /**
   * The current leading player
   */
  private final Player leadingPlayer;
  private final Set<Player> players;
  private final String gameID;

  /**
   * @param availableNobles The nobles on the board
   * @param cards           The cards on the board, including the one's that are not face up.
   *                        I'm interpreting it as each list being a list of a certain level of cards,
   *                        with the first x amount in each list being the cards that are face up.
   *                        We can use the sublist method for this I believe.
   * @param expansions      The set of expansions
   * @param players         The players
   * @param gameID          The game ID
   */
  public GameBoard(Set<Noble> availableNobles, List<List<Card>> cards, Set<Expansion> expansions,
                   Set<Player> players, String gameID) {
    playerTurnMap = new HashMap<>();
    int count = 0;
    for (Player player : players) {
      playerTurnMap.put(count++, player);
    }
    playerTurn = new Random().nextInt(0, count);
    leadingPlayer = playerTurnMap.get(0);

    availableGems = new Gems();
    for (GemColor gemColor : GemColor.values()) {
      availableGems.put(gemColor, 3);
    }

    this.availableNobles = availableNobles;
    this.cards = cards;
    this.expansions = expansions;
    this.players = players;
    this.gameID = gameID;
  }

  public int playerTurn() {
    return playerTurn;
  }

  public Map<Integer, Player> playerTurnMap() {
    return playerTurnMap;
  }

  public Set<Noble> availableNobles() {
    return availableNobles;
  }

  public Gems availableGems() {
    return availableGems;
  }

  public List<List<Card>> cards() {
    return cards;
  }

  public Set<Expansion> expansions() {
    return expansions;
  }

  public Player leadingPlayer() {
    return leadingPlayer;
  }

  public Set<Player> players() {
    return players;
  }

  public String gameID() {
    return gameID;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (GameBoard) obj;
    return this.playerTurn == that.playerTurn &&
           Objects.equals(this.playerTurnMap, that.playerTurnMap) &&
           Objects.equals(this.availableNobles, that.availableNobles) &&
           Objects.equals(this.availableGems, that.availableGems) &&
           Objects.equals(this.cards, that.cards) &&
           Objects.equals(this.expansions, that.expansions) &&
           Objects.equals(this.leadingPlayer, that.leadingPlayer) &&
           Objects.equals(this.players, that.players) && Objects.equals(this.gameID, that.gameID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerTurn, playerTurnMap, availableNobles, availableGems, cards,
        expansions, leadingPlayer, players, gameID);
  }

  @Override
  public String toString() {
    return "GameBoard[" + "playerTurn=" + playerTurn + ", " + "playerTurnMap=" + playerTurnMap +
           ", " + "availableNobles=" + availableNobles + ", " + "availableGems=" + availableGems +
           ", " + "cards=" + cards + ", " + "expansions=" + expansions + ", " + "leadingPlayer=" +
           leadingPlayer + ", " + "players=" + players + ", " + "gameID=" + gameID + ']';
  }

}
