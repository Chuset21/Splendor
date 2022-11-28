package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Sacrifice Card.
 */
public final class SacrificeCard extends Card {
  private int gemDiscount;
  private GemColor discountColor;
  private GemColor sacrificeColor;

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

  /**
   * No args constructor.
   */
  public SacrificeCard() {
    super();
  }

  /**
   * A Getter for the Gem Discount associated with the Sacrifice Card.
   *
   * @return The Gem Discount.
   */
  public int gemDiscount() {
    return gemDiscount;
  }

  /**
   * A Getter for the Discount Color associated with the Sacrifice Card.
   *
   * @return The Discount Color.
   */
  public GemColor discountColor() {
    return discountColor;
  }

  /**
   * A Getter for the Color to be Sacrificed to acquire the Sacrifice Card.
   *
   * @return The Sacrifice Color.
   */
  public GemColor sacrificeColor() {
    return sacrificeColor;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    SacrificeCard card = (SacrificeCard) obj;
    return super.prestigePoints == card.prestigePoints && super.cost.equals(card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && gemDiscount == card.gemDiscount && discountColor == card.discountColor
           && sacrificeColor == card.sacrificeColor;
  }
}
