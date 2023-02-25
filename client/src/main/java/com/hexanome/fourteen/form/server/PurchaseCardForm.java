package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.payment.PaymentForm;

/**
 * Purchase card form.
 */
public final class PurchaseCardForm {
  private CardForm card;
  private PaymentForm payment;
  private boolean isReserved;

  /**
   * Constructor.
   *
   * @param card       card to be purchased
   * @param payment    the payment to pay for the card
   * @param isReserved whether the card is reserved or not
   */
  public PurchaseCardForm(CardForm card, PaymentForm payment, boolean isReserved) {
    this.card = card;
    this.payment = payment;
    this.isReserved = isReserved;
  }

  /**
   * No args constructor.
   */
  public PurchaseCardForm() {
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
   * A Getter for payment.
   *
   * @return Chosen payment
   */
  public PaymentForm payment() {
    return payment;
  }

  public boolean isReserved() {
    return isReserved;
  }
}
