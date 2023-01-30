package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.boards.GemColor;
import com.hexanome.fourteen.form.server.cardform.CardForm;

/**
 * Reserve card form.
 */
public final class ReserveCardForm {

  private CardForm card;
  private GemColor gemColor;

  /**
   * Constructor.
   *
   * @param card     card to be reserved.
   * @param gemColor the singular gem to be discarded
   *                 (optional, only applies if the player has 10 gems).
   */
  public ReserveCardForm(CardForm card, GemColor gemColor) {
    this.card = card;
    this.gemColor = gemColor;
  }

  /**
   * No args constructor.
   */
  public ReserveCardForm() {

  }

  /**
   * A Getter for Card.
   *
   * @return Card
   */
  public CardForm card() {
    return card;
  }

  /**
   * A Getter for GemColor.
   *
   * @return the gem color
   */
  public GemColor gemColor() {
    return gemColor;
  }
}
