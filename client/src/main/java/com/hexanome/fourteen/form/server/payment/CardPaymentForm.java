package com.hexanome.fourteen.form.server.payment;

import com.hexanome.fourteen.form.server.cardform.CardForm;

/**
 * A class to represent payment using cards as required by sacrifice cards.
 */
public final class CardPaymentForm implements PaymentForm {

  private CardForm cardToSacrifice1;

  private CardForm cardToSacrifice2;

  /**
   * Constructor.
   *
   * @param cardToSacrifice1 First card to sacrifice.
   * @param cardToSacrifice2 Second card to sacrifice.
   */
  public CardPaymentForm(CardForm cardToSacrifice1, CardForm cardToSacrifice2) {
    this.cardToSacrifice1 = cardToSacrifice1;
    this.cardToSacrifice2 = cardToSacrifice2;
  }

  /**
   * No args constructor.
   */
  public CardPaymentForm() {

  }

  /**
   * A getter for the first card the player is sacrificing.
   *
   * @return the first card being sacrificed.
   */
  public CardForm getCardToSacrifice1() {
    return cardToSacrifice1;
  }

  /**
   * A getter for the second card the player is sacrificing.
   *
   * @return the second card being sacrificed.
   */
  public CardForm getCardToSacrifice2() {
    return cardToSacrifice2;
  }
}
