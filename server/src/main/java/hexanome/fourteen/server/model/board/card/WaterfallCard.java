package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Waterfall Card.
 */
public final class WaterfallCard extends Card {
  private int gemDiscount;
  private GemColor discountColor;
  private CardLevel freeCardLevel;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param gemDiscount    the amount of gems to be discounted
   * @param discountColor  the color of the gems to be discounted
   * @param freeCardLevel  the level of the card that you get for free
   */
  public WaterfallCard(int prestigePoints, Gems cost, CardLevel level, Expansion expansion,
                       int gemDiscount, GemColor discountColor, CardLevel freeCardLevel) {
    super(prestigePoints, cost, level, expansion);
    this.gemDiscount = gemDiscount;
    this.discountColor = discountColor;
    this.freeCardLevel = freeCardLevel;
  }

  /**
   * No args constructor.
   */
  public WaterfallCard() {
    super();
  }

  /**
   * A Getter for the Gem Discount of a WaterfallCard.
   *
   * @return The Gem Discount
   */
  public int gemDiscount() {
    return gemDiscount;
  }

  /**
   * A Getter for the Discount Color of a WaterfallCard.
   *
   * @return The Discount Color
   */
  public GemColor discountColor() {
    return discountColor;
  }


  /**
   * A Getter for the Free Card Level of a WaterfallCard.
   *
   * @return The Free Card Level
   */
  public CardLevel freeCardLevel() {
    return freeCardLevel;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    WaterfallCard card = (WaterfallCard) obj;
    return super.prestigePoints == card.prestigePoints && super.cost.equals(card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && gemDiscount == card.gemDiscount && discountColor == card.discountColor
           && freeCardLevel == card.freeCardLevel;
  }
}
