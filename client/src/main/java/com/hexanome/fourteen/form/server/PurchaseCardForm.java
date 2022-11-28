package com.hexanome.fourteen.form.server;

/**
 * Purchase card form.
 */
public final class PurchaseCardForm {
  private CardForm card;
  private GemsForm chosenGems;
  private GemsForm substitutedGems;

  /**
   * Constructor.
   *
   * @param card            card to be purchased
   * @param chosenGems      gems used to pay for the card.
   * @param substitutedGems the chosen gems to be substituted for gold gems
   */
  public PurchaseCardForm(CardForm card, GemsForm chosenGems, GemsForm substitutedGems) {
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
  public CardForm card() {
    return card;
  }

  /**
   * A Getter for Gems a Player has decided to pay with.
   *
   * @return Chosen Gems
   */
  public GemsForm gemsToPayWith() {
    return chosenGems;
  }

  /**
   * A Getter for Gems that will be substituted for Gold Gems.
   *
   * @return Chosen Gems
   */
  public GemsForm substitutedGems() {
    return substitutedGems;
  }
}
