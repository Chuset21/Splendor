package com.hexanome.fourteen.form.server.cardform;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.boards.GemColor;
import com.hexanome.fourteen.form.server.GemsForm;

/**
 * Reserve Noble Card form.
 */
public final class ReserveNobleCardForm extends CardForm {
  private int gemDiscount;
  private GemColor discountColor;

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
  public ReserveNobleCardForm(int prestigePoints, GemsForm cost,
                              CardLevelForm level,
                              Expansion expansion, int gemDiscount, GemColor discountColor) {
    super(prestigePoints, cost, level, expansion);
    this.gemDiscount = gemDiscount;
    this.discountColor = discountColor;
  }

  /**
   * No args constructor.
   */
  public ReserveNobleCardForm() {
    super();
  }

  /**
   * A Getter for the Gem Discount.
   *
   * @return Gem Discount
   */
  public int gemDiscount() {
    return gemDiscount;
  }

  /**
   * A Getter for the Color of the Gem which is allowing a Discount.
   *
   * @return Gem Discount
   */
  public GemColor discountColor() {
    return discountColor;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ReserveNobleCardForm card = (ReserveNobleCardForm) obj;
    return super.prestigePoints == card.prestigePoints && super.cost.equals(card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && gemDiscount == card.gemDiscount && discountColor == card.discountColor;
  }
}
