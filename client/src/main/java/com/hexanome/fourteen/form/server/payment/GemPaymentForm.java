package com.hexanome.fourteen.form.server.payment;

import com.hexanome.fourteen.form.server.GemsForm;

/**
 * A class to represent payment using Gems.
 */
public final class GemPaymentForm implements PaymentForm {

  private GemsForm chosenGems;

  private int numGoldGemCards;

  /**
   * Constructor.
   *
   * @param chosenGems      chosen Gems to pay with
   * @param numGoldGemCards number of goldGemCards chosen
   */
  public GemPaymentForm(GemsForm chosenGems, int numGoldGemCards) {
    this.chosenGems = chosenGems;
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
   * A getter for the number of goldGemCards used for payment.
   *
   * @return number of goldGemCards
   */
  public int getNumGoldGemCards() {
    return numGoldGemCards;
  }
}
