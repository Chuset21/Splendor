package com.hexanome.fourteen.form.server.cardform;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.boards.GemColor;
import com.hexanome.fourteen.form.server.GemsForm;
import java.util.Objects;

/**
 * Sacrifice Card form.
 */
public final class SacrificeCardForm extends CardForm {
  private GemColor discountColor;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card, this should be empty or null
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param discountColor  the color of the gems to be discounted
   */
  public SacrificeCardForm(int prestigePoints, GemsForm cost,
                           CardLevelForm level, Expansion expansion, GemColor discountColor) {
    super(prestigePoints, cost, level, expansion);
    this.discountColor = discountColor;
  }

  /**
   * No args constructor.
   */
  public SacrificeCardForm() {
    super();
  }

  /**
   * A Getter for the Discount Color associated with the Sacrifice Card.
   *
   * @return The Discount Color.
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
    SacrificeCardForm card = (SacrificeCardForm) obj;
    return super.prestigePoints == card.prestigePoints
           && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && discountColor == card.discountColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost, level, expansion, discountColor);
  }
}
