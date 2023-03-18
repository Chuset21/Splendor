package hexanome.fourteen.server.control.form.payment;

import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * A class to represent payment using Gems.
 */
public final class GemPayment implements Payment {

  private Gems chosenGems;

  private int numGoldGemCards;

  /**
   * Constructor.
   *
   * @param chosenGems        chosen Gems to pay with
   * @param numGoldGemCards   number of goldGemCards chosen
   */
  public GemPayment(Gems chosenGems, int numGoldGemCards) {
    this.chosenGems = chosenGems;
    this.numGoldGemCards = numGoldGemCards;
  }

  /**
   * No args constructor.
   */
  public GemPayment() {

  }

  /**
   * A getter for the chosen Gems to pay with.
   *
   * @return chosen Gems
   */
  public Gems getChosenGems() {
    return chosenGems;
  }

  /**
   * A getter for the number of goldGemCards used for payment.
   *
   * @return number of goldGemCards
   */
  public int getNumGoldGemCards() {
    return numGoldGemCards;
  }
}
