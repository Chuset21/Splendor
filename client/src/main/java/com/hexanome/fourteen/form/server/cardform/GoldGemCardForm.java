package com.hexanome.fourteen.form.server.cardform;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.form.server.GemsForm;
import java.util.Objects;

/**
 * Gold Gem Card form.
 */
public final class GoldGemCardForm extends CardForm {

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   */
  public GoldGemCardForm(int prestigePoints, GemsForm cost, CardLevelForm level,
                         Expansion expansion) {
    super(prestigePoints, cost, level, expansion);
  }

  /**
   * No args constructor.
   */
  public GoldGemCardForm() {
    super();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    GoldGemCardForm card = (GoldGemCardForm) obj;
    return super.prestigePoints == card.prestigePoints && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion;
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost, level, expansion);
  }
}
