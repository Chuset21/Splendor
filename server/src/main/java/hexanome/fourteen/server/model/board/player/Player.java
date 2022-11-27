package hexanome.fourteen.server.model.board.player;

import hexanome.fourteen.server.model.board.Hand;

/**
 * Player.
 */
public final class Player {
  private final String uid;
  private final Hand hand;

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
   * @param id ID of the Player
   */
  public Player(String id) {
    this(id, new Hand());
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
