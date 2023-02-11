package hexanome.fourteen.server.control.form;

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

  /**
   * A Getter for Card.
   *
   * @return Card
   */
  public Card card() {
    return card;
  }

  /**
   * A Getter for Gems a Player has decided to pay with.
   *
   * @return Chosen Gems
   */
  public Gems gemsToPayWith() {
    return chosenGems;
  }

  /**
   * A Getter for Gems that will be substituted for Gold Gems.
   *
   * @return Chosen Gems
   */
  public Gems substitutedGems() {
    return substitutedGems;
  }
}
