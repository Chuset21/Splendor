package com.hexanome.fourteen.form.server.cardform;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.form.server.GemsForm;
import java.util.Objects;

/**
 * Satchel Card form.
 */
public final class SatchelCardForm extends CardForm {
  private CardForm cardToAttach;
  private CardForm freeCardToTake;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param cardToAttach   the card to attach
   * @param freeCardToTake the card to take for free, if this card is a level two card
   */
  public SatchelCardForm(int prestigePoints, GemsForm cost, CardLevelForm level, Expansion expansion,
                     CardForm cardToAttach, CardForm freeCardToTake) {
    super(prestigePoints, cost, level, expansion);
    this.cardToAttach = cardToAttach;
    this.freeCardToTake = freeCardToTake;
  }

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param cardToAttach   the card to attach
   */
  public SatchelCardForm(int prestigePoints, GemsForm cost, CardLevelForm level, Expansion expansion,
                     CardForm cardToAttach) {
    this(prestigePoints, cost, level, expansion, cardToAttach, null);
  }


  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   */
  public SatchelCardForm(int prestigePoints, GemsForm cost, CardLevelForm level, Expansion expansion) {
    this(prestigePoints, cost, level, expansion, null);
  }

  /**
   * No args constructor.
   */
  public SatchelCardForm() {
    super();
  }

  public CardForm cardToAttach() {
    return cardToAttach;
  }

  public CardForm freeCardToTake() {
    return freeCardToTake;
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
    return super.prestigePoints == card.prestigePoints && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && Objects.equals(cardToAttach, card.cardToAttach)
           && Objects.equals(freeCardToTake, card.freeCardToTake);
  }
}
