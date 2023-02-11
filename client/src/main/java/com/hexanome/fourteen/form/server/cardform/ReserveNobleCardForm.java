package com.hexanome.fourteen.form.server.cardform;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.boards.GemColor;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.NobleForm;
import java.util.Objects;

/**
 * Reserve Noble Card form.
 */
public final class ReserveNobleCardForm extends CardForm {
  private int gemDiscount;
  private GemColor discountColor;
  private NobleForm nobleToReserve;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param gemDiscount    the amount of gems to be discounted
   * @param discountColor  the color of the gems to be discounted
   * @param nobleToReserve the noble to be reserved
   */
  public ReserveNobleCardForm(int prestigePoints, GemsForm cost,
                          CardLevelForm level,
                          Expansion expansion, int gemDiscount, GemColor discountColor,
                          NobleForm nobleToReserve) {
    super(prestigePoints, cost, level, expansion);
    this.gemDiscount = gemDiscount;
    this.discountColor = discountColor;
    this.nobleToReserve = nobleToReserve;
  }

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
    this(prestigePoints, cost, level, expansion, gemDiscount, discountColor, null);
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
   * @return discount color
   */
  public GemColor discountColor() {
    return discountColor;
  }

  public NobleForm nobleToReserve() {
    return nobleToReserve;
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
    return super.prestigePoints == card.prestigePoints && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && gemDiscount == card.gemDiscount && discountColor == card.discountColor
           && Objects.equals(nobleToReserve, card.nobleToReserve);
  }
}
