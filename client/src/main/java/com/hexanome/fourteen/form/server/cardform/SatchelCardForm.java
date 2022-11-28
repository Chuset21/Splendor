package com.hexanome.fourteen.form.server.cardform;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.boards.GemColor;
import com.hexanome.fourteen.form.server.GemsForm;

/**
 * Satchel Card form.
 */
public final class SatchelCardForm extends CardForm {
  private GemColor gemColor;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param gemColor       the gem color to attach the satchel card to
   */
  public SatchelCardForm(int prestigePoints, GemsForm cost, CardLevelForm level, Expansion expansion,
                         GemColor gemColor) {
    super(prestigePoints, cost, level, expansion);
    this.gemColor = gemColor;
  }

  /**
   * No args constructor.
   */
  public SatchelCardForm() {
    super();
  }

  /**
   * A Getter for the Gem Color of the Satchel Card.
   *
   * @return The Gem Color
   */
  public GemColor gemColor() {
    return gemColor;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    SatchelCardForm card = (SatchelCardForm) obj;
    return super.prestigePoints == card.prestigePoints && super.cost.equals(card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && gemColor == card.gemColor;
  }
}
