package hexanome.fourteen.server.control.form.payment;

import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * A class to represent payment using Gems.
 */
public final class GemPayment implements Payment {

  private Gems chosenGems;

  private Gems substitutedGems;

  private int numGoldGemCards;

  /**
   * Constructor.
   *
   * @param chosenGems        chosen Gems to pay with
   * @param substitutedGems   required gems provided through goldGemCards
   * @param numGoldGemCards   number of goldGemCards chosen
   */
  public GemPayment(Gems chosenGems, Gems substitutedGems, int numGoldGemCards) {
    this.chosenGems = chosenGems;
    this.substitutedGems = substitutedGems;
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
   * A getter for the number of substituted gems.
   *
   * @return substituted Gems
   */
  public Gems getSubstitutedGems() {
    return substitutedGems;
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
