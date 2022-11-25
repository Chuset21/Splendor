package hexanome.fourteen.server.model.board;

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

  public Player(String id) {
    this(id, new Hand());
  }

  public String uid() {
    return uid;
  }

  public Hand hand() {
    return hand;
  }
}
