package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Card.
 */
public abstract class Card {
  private final int prestigePoints;
  private final Gems cost;
  private final CardLevel level;
  private final Expansion expansion;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   */
  public Card(int prestigePoints, Gems cost, CardLevel level, Expansion expansion) {
    this.prestigePoints = prestigePoints;
    this.cost = cost;
    this.level = level;
    this.expansion = expansion;
  }

  public int prestigePoints() {
    return prestigePoints;
  }

  public Gems cost() {
    return cost;
  }

  public CardLevel level() {
    return level;
  }

  public Expansion expansion() {
    return expansion;
  }
}
