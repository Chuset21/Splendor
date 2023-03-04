package hexanome.fourteen.server.model.board.card;

/**
 * Bonus enum.
 */
public enum Bonus {
  /**
   * Double bonus.
   */
  DOUBLE(2),
  /**
   * Single bonus.
   */
  SINGLE(1);

  private final int value;

  Bonus(int value) {
    this.value = value;
  }

  /**
   * Get the value of a bonus.
   *
   * @return The value of the bonus.
   */
  public int getValue() {
    return value;
  }
}
