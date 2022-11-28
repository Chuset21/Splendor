package hexanome.fourteen.server.control;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Purchase card form.
 */
public final class PurchaseCardForm {
  private Card card;
  private Gems chosenGems;
  private Gems substitutedGems;

  /**
   * Constructor.
   *
   * @param card            card to be purchased
   * @param chosenGems      gems used to pay for the card.
   * @param substitutedGems the chosen gems to be substituted for gold gems
   */
  public PurchaseCardForm(Card card, Gems chosenGems, Gems substitutedGems) {
    this.card = card;
    this.chosenGems = chosenGems;
    this.substitutedGems = substitutedGems;
  }

  /**
   * No args constructor.
   */
  public PurchaseCardForm() {
  }

  public Card card() {
    return card;
  }

  public Gems gemsToPayWith() {
    return chosenGems;
  }

  public Gems substitutedGems() {
    return substitutedGems;
  }
}
