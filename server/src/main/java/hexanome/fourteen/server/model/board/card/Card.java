package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Card.
 */
public abstract class Card {
  int prestigePoints;
  Gems cost;
  CardLevel level;
  Expansion expansion;

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

  /**
   * No args constructor.
   */
  public Card() {
  }

  /**
   * A Getter to get the Prestige Points of the Card.
   *
   * @return Prestige Points of the Card
   */
  public int prestigePoints() {
    return prestigePoints;
  }

  /**
   * A Getter to get the Cost of the Card.
   *
   * @return Cost of the Card
   */
  public Gems cost() {
    return cost;
  }

  /**
   * A Getter to get the Level of the Card.
   *
   * @return Level of the Card
   */
  public CardLevel level() {
    return level;
  }

  /**
   * A Getter to get which Expansion in which the Card is a part of.
   *
   * @return Expansion of the Card
   */
  public Expansion expansion() {
    return expansion;
  }
}
