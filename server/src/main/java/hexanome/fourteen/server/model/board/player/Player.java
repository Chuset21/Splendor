package hexanome.fourteen.server.model.board.player;

import hexanome.fourteen.server.model.board.Hand;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import java.util.Set;

/**
 * Player.
 */
public final class Player {
  private String uid;
  private Hand hand;

  /**
   * Constructor.
   *
   * @param id   the player's user ID
   * @param hand the player's hand
   */
  public Player(String id, Hand hand) {
    this.uid = id;
    this.hand = hand;
  }

  /**
   * Constructor when given just the ID.
   *
   * @param id         ID of the Player
   * @param expansions The chosen expansions
   */
  public Player(String id, Set<Expansion> expansions) {
    this(id, new Hand(expansions));
  }

  /**
   * No args constructor.
   */
  public Player() {

  }

  /**
   * A Getter for the User ID.
   *
   * @return The User ID
   */
  public String uid() {
    return uid;
  }

  /**
   * A Getter for the Hand.
   *
   * @return The Hand
   */
  public Hand hand() {
    return hand;
  }
}
