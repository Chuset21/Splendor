package com.hexanome.fourteen.form.server.cardform;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.boards.GemColor;
import com.hexanome.fourteen.form.server.GemsForm;

/**
 * Waterfall Card form.
 */
public final class WaterfallCardForm extends CardForm {
  private int gemDiscount;
  private GemColor discountColor;
  private CardLevelForm freeCardLevel;

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
  public WaterfallCardForm(int prestigePoints, GemsForm cost, CardLevelForm level, Expansion expansion,
                           int gemDiscount, GemColor discountColor, CardLevelForm freeCardLevel) {
    super(prestigePoints, cost, level, expansion);
    this.gemDiscount = gemDiscount;
    this.discountColor = discountColor;
    this.freeCardLevel = freeCardLevel;
  }

  /**
   * No args constructor.
   */
  public WaterfallCardForm() {
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
  public CardLevelForm freeCardLevel() {
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
    WaterfallCardForm card = (WaterfallCardForm) obj;
    return super.prestigePoints == card.prestigePoints && super.cost.equals(card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && gemDiscount == card.gemDiscount && discountColor == card.discountColor
           && freeCardLevel == card.freeCardLevel;
  }
}
