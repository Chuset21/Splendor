package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.form.server.cardform.CardForm;
import java.util.Set;

/**
 * Hand form.
 */
public final class HandForm {
  private GemsForm gems;
  private Set<CardForm> reservedCards;
  private Set<CardForm> purchasedCards;
  private NobleForm visitedNoble;
  private NobleForm reservedNoble;
  private GemsForm gemDiscounts;

  /**
   * Constructor.
   *
   * @param gems           the amount of gems the owned
   * @param reservedCards  the reserved cards
   * @param purchasedCards the purchased cards
   * @param visitedNoble   the owned noble
   * @param reservedNoble  the reserved noble
   * @param gemDiscounts   the gem discounts
   */
  public HandForm(GemsForm gems, Set<CardForm> reservedCards, Set<CardForm> purchasedCards,
                  NobleForm visitedNoble, NobleForm reservedNoble, GemsForm gemDiscounts) {
    this.gems = gems;
    this.reservedCards = reservedCards;
    this.purchasedCards = purchasedCards;
    this.visitedNoble = visitedNoble;
    this.reservedNoble = reservedNoble;
    this.gemDiscounts = gemDiscounts;
  }

  /**
   * No args constructor.
   */
  public HandForm() {
  }

  /**
   * A Getter for the Gems.
   *
   * @return Gems owned.
   */
  public GemsForm gems() {
    return gems;
  }

  /**
   * A Getter for the Reserved Cards.
   *
   * @return Cards reserved.
   */
  public Set<CardForm> reservedCards() {
    return reservedCards;
  }

  /**
   * A Getter for the Purchased Cards.
   *
   * @return Cards purchased.
   */
  public Set<CardForm> purchasedCards() {
    return purchasedCards;
  }

  /**
   * A Getter for the Noble that is visiting.
   *
   * @return The Noble that is visiting.
   */
  public NobleForm visitedNoble() {
    return visitedNoble;
  }

  /**
   * A Getter for the Noble that has been reserved.
   *
   * @return The Noble that has been reserved.
   */
  public NobleForm reservedNoble() {
    return reservedNoble;
  }

  /**
   * A Getter for the Gem Discounts the Player currently has.
   *
   * @return The Gem Discounts the Player currently has.
   */
  public GemsForm gemDiscounts() {
    return gemDiscounts;
  }
}
