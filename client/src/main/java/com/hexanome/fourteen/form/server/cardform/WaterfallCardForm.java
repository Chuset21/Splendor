package com.hexanome.fourteen.form.server.cardform;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.boards.GemColor;
import com.hexanome.fourteen.form.server.GemsForm;
import java.util.Objects;

/**
 * Waterfall Card form.
 */
public final class WaterfallCardForm extends CardForm {
  private GemColor discountColor;
  private CardForm cardToTake;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param discountColor  the color of the gems to be discounted
   * @param cardToTake     the card to take for free
   */
  public WaterfallCardForm(int prestigePoints, GemsForm cost, CardLevelForm level,
                           Expansion expansion, GemColor discountColor, CardForm cardToTake) {
    super(prestigePoints, cost, level, expansion);
    this.discountColor = discountColor;
    this.cardToTake = cardToTake;
  }

  public WaterfallCardForm(WaterfallCardForm wf, CardForm cardToTake) {
    this(wf.prestigePoints, wf.cost, wf.level, wf.expansion, wf.discountColor, cardToTake);
  }

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param discountColor  the color of the gems to be discounted
   */
  public WaterfallCardForm(int prestigePoints, GemsForm cost, CardLevelForm level,
                           Expansion expansion, GemColor discountColor) {
    this(prestigePoints, cost, level, expansion, discountColor, null);
  }

  /**
   * No args constructor.
   */
  public WaterfallCardForm() {
    super();
  }

  /**
   * A Getter for the Discount Color of a WaterfallCard.
   *
   * @return The Discount Color
   */
  public GemColor discountColor() {
    return discountColor;
  }

  public CardForm cardToTake() {
    return cardToTake;
  }

  public void setCardToTake(CardForm cardToTake) {
    this.cardToTake = cardToTake;
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
    return super.prestigePoints == card.prestigePoints && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && discountColor == card.discountColor && Objects.equals(cardToTake, card.cardToTake);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost, level, expansion, discountColor, cardToTake);
  }
}
