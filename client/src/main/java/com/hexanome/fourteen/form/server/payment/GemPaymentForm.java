package com.hexanome.fourteen.form.server.payment;

import com.hexanome.fourteen.form.server.GemsForm;

/**
 * A class to represent payment using Gems.
 */
public class GemPaymentForm implements PaymentForm {

  private GemsForm chosenGems;

  private GemsForm substitutedGems;

  private int numGoldGemCards;

  /**
   * Constructor.
   *
   * @param chosenGems        chosen Gems to pay with
   * @param substitutedGems   required gems provided through goldGemCards
   * @param numGoldGemCards   number of goldGemCards chosen
   */
  public GemPaymentForm(GemsForm chosenGems, GemsForm substitutedGems, int numGoldGemCards) {
    this.chosenGems = chosenGems;
    this.substitutedGems = substitutedGems;
    this.numGoldGemCards = numGoldGemCards;
  }

  /**
   * No args constructor.
   */
  public GemPaymentForm() {

  }

  /**
   * A getter for the chosen Gems to pay with.
   *
   * @return chosen Gems
   */
  public GemsForm getChosenGems() {
    return chosenGems;
  }

  /**
   * A getter for the number of substituted gems.
   *
   * @return substituted Gems
   */
  public GemsForm getSubstitutedGems() {
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
