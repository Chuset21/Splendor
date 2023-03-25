package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.payment.PaymentForm;
import com.hexanome.fourteen.form.server.tradingposts.TradingPostTakeGem;

/**
 * Purchase card form.
 */
public final class PurchaseCardForm {
  private CardForm card;
  private PaymentForm payment;
  private boolean isReserved;
  private TradingPostTakeGem tradingPostTakeGem;

  /**
   * Constructor.
   *
   * @param card       card to be purchased
   * @param payment    the payment to pay for the card
   * @param isReserved whether the card is reserved or not
   */
  public PurchaseCardForm(CardForm card, PaymentForm payment, boolean isReserved) {
    this(card, payment, isReserved, null);
  }

  /**
   * Constructor.
   *
   * @param card               Card to be purchased
   * @param payment            The payment to pay for the card
   * @param isReserved         Whether the card is reserved or not
   * @param tradingPostTakeGem The trading posts take game, null if not needed.
   */
  public PurchaseCardForm(CardForm card, PaymentForm payment, boolean isReserved,
                          TradingPostTakeGem tradingPostTakeGem) {
    this.card = card;
    this.payment = payment;
    this.isReserved = isReserved;
    this.tradingPostTakeGem = tradingPostTakeGem;
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

  /**
   * Getter for whether the card is reserved.
   *
   * @return true if it is reserved, false otherwise.
   */
  public boolean isReserved() {
    return isReserved;
  }

  /**
   * Getter for trading post take gem.
   *
   * @return The trading post take gem.
   */
  public TradingPostTakeGem tradingPostTakeGem() {
    return tradingPostTakeGem;
  }

  public void setTradingPostTakeGem(TradingPostTakeGem bonusGem) {
    this.tradingPostTakeGem = bonusGem;
  }
}
