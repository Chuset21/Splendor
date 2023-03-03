package hexanome.fourteen.server.control.form.payment;

import hexanome.fourteen.server.model.board.card.Card;

/**
 * A class to represent payment using cards as required by sacrifice cards.
 */
public final class CardPayment implements Payment {

  private Card cardToSacrifice1;

  private Card cardToSacrifice2;

  /**
   * Constructor.
   *
   * @param cardToSacrifice1 First card to sacrifice.
   * @param cardToSacrifice2 Second card to sacrifice.
   */
  public CardPayment(Card cardToSacrifice1, Card cardToSacrifice2) {
    this.cardToSacrifice1 = cardToSacrifice1;
    this.cardToSacrifice2 = cardToSacrifice2;
  }

  /**
   * No args constructor.
   */
  public CardPayment() {

  }

  /**
   * A getter for the first card the player is sacrificing.
   *
   * @return the first card being sacrificed.
   */
  public Card getCardToSacrifice1() {
    return cardToSacrifice1;
  }

  /**
   * A getter for the second card the player is sacrificing.
   *
   * @return the second card being sacrificed.
   */
  public Card getCardToSacrifice2() {
    return cardToSacrifice2;
  }
}
