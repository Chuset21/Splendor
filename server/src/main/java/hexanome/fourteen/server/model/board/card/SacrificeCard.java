package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Sacrifice Card.
 */
public final class SacrificeCard extends Card {
  private final int gemDiscount;
  private final GemColor discountColor;
  private final GemColor sacrificeColor;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param gemDiscount    the amount of gems to be discounted
   * @param discountColor  the color of the gems to be discounted
   * @param sacrificeColor the gem color of the cards to be sacrificed
   */
  public SacrificeCard(int prestigePoints, Gems cost,
                       CardLevel level,
                       Expansion expansion, int gemDiscount, GemColor discountColor,
                       GemColor sacrificeColor) {
    super(prestigePoints, cost, level, expansion);
    this.gemDiscount = gemDiscount;
    this.discountColor = discountColor;
    this.sacrificeColor = sacrificeColor;
  }


  public int gemDiscount() {
    return gemDiscount;
  }

  public GemColor discountColor() {
    return discountColor;
  }

  public GemColor sacrificeColor() {
    return sacrificeColor;
  }
}
