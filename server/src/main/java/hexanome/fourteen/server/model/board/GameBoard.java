package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Game Board.
 * We might have to include a different class, GameBoard, with no functionality,
 * and only the necessary information to send to the clients (We'll see).
 * If so this might be true for more than just this class.
 */
public final class GameBoard {
  /**
   * The current player's turn.
   */
  private final int playerTurn;
  /**
   * A map to convert player turn to the actual player.
   */
  private final Map<Integer, Player> playerTurnMap;
  private final Set<Noble> availableNobles;
  /**
   * The gems in the bank.
   */
  private final Gems availableGems;
  private final List<List<Card>> cards;
  private final Set<Expansion> expansions;
  /**
   * The current leading player.
   */
  private final Player leadingPlayer;
  private final Set<Player> players;
  private final String gameid;
  private final String creator;

  /**
   * Constructor.
   *
   * @param availableNobles The nobles on the board
   * @param cards           The cards on the board, including the one's that are not face up.
   *                        I'm interpreting it as each list being a list of a certain
   *                        level of cards, with the first x amount in each list being the cards
   *                        that are face up.
   *                        We can use the sublist method for this I believe.
   * @param expansions      The set of expansions
   * @param players         The players
   * @param gameid          The game ID
   * @param creator         The creator of the game
   */
  public GameBoard(Set<Noble> availableNobles, List<List<Card>> cards, Set<Expansion> expansions,
                   Set<Player> players, String gameid, String creator) {
    playerTurnMap = new HashMap<>();
    int count = 0;
    for (Player player : players) {
      playerTurnMap.put(count++, player);
    }
    playerTurn = new Random().nextInt(0, count);
    leadingPlayer = playerTurnMap.get(0);

    availableGems = new Gems();
    for (GemColor gemColor : GemColor.values()) {
      availableGems.put(gemColor, 10);
    }
    availableGems.put(GemColor.GOLD, 5);

    this.availableNobles = availableNobles;
    this.cards = cards;
    this.expansions = expansions;
    this.players = players;
    this.gameid = gameid;
    this.creator = creator;
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

  public String gameid() {
    return gameid;
  }

  public String creator() {
    return creator;
  }
}
