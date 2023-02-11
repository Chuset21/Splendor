package hexanome.fourteen.server.control.form;

import hexanome.fourteen.server.control.form.payment.Payment;
import hexanome.fourteen.server.model.board.card.Card;

/**
 * Purchase card form.
 */
public final class PurchaseCardForm {
  private Card card;
  private Payment payment;
  private boolean isReserved;

  /**
   * Constructor.
   *
   * @param card       card to be purchased
   * @param payment    the payment to pay for the card
   * @param isReserved whether the card is reserved or not
   */
  public PurchaseCardForm(Card card, Payment payment, boolean isReserved) {
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

  public boolean isReserved() {
    return isReserved;
  }
}
