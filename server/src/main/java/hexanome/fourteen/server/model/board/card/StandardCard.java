package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Standard Card.
 */
public final class StandardCard extends Card {
  private final int gemDiscount;
  private final GemColor discountColor;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param gemDiscount    the amount of gems to be discounted
   * @param discountColor  the color of the gems to be discounted
   */
  public StandardCard(int prestigePoints, Gems cost, CardLevel level, Expansion expansion,
                      int gemDiscount, GemColor discountColor) {
    super(prestigePoints, cost, level, expansion);
    this.gemDiscount = gemDiscount;
    this.discountColor = discountColor;
  }

  /**
   * A Getter for the Gem Discount associated with the StandardCard.
   *
   * @return The Gem Discount
   */
  public int gemDiscount() {
    return gemDiscount;
  }

  /**
   * A Getter for the Discount Color associated with the StandardCard.
   *
   * @return The Discount Color
   */
  public GemColor discountColor() {
    return discountColor;
  }
}