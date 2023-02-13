package hexanome.fourteen.server.model.board.card;

/**
 * Bonus enum.
 */
public enum Bonus {
  DOUBLE(2), SINGLE(1);

  private final int value;

  Bonus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
