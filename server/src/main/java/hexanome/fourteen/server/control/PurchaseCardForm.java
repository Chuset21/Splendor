package hexanome.fourteen.server.control;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Purchase card form.
 */
public final class PurchaseCardForm {
  private Card card;
  private Gems chosenGems;

  /**
   * Constructor.
   *
   * @param card       card to be purchased
   * @param chosenGems gems used to pay for the card.
   */
  public PurchaseCardForm(Card card, Gems chosenGems) {
    this.card = card;
    this.chosenGems = chosenGems;
  }

  /**
   * No args constructor.
   */
  public PurchaseCardForm() {
  }

  public Card card() {
    return card;
  }

  public Gems chosenGems() {
    return chosenGems;
  }
}
