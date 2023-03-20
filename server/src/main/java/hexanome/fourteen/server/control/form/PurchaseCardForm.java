package hexanome.fourteen.server.control.form;

import hexanome.fourteen.server.control.form.payment.Payment;
import hexanome.fourteen.server.control.form.tradingpost.TradingPostTakeGem;
import hexanome.fourteen.server.model.board.card.Card;

/**
 * Purchase card form.
 */
public final class PurchaseCardForm {
  private Card card;
  private Payment payment;
  private boolean isReserved;
  private TradingPostTakeGem tradingPostTakeGem;

  /**
   * Constructor.
   *
   * @param card       card to be purchased
   * @param payment    the payment to pay for the card
   * @param isReserved whether the card is reserved or not
   */
  public PurchaseCardForm(Card card, Payment payment, boolean isReserved) {
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
  public PurchaseCardForm(Card card, Payment payment, boolean isReserved,
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
  public Card card() {
    return card;
  }

  /**
   * A Getter for payment.
   *
   * @return Chosen payment
   */
  public Payment payment() {
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
}
