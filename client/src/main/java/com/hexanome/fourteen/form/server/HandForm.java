package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.form.server.cardform.CardForm;
import java.util.List;
import java.util.Set;

/**
 * Hand form.
 */
public final class HandForm {
  private GemsForm gems;
  private List<CardForm> reservedCards;
  private List<CardForm> purchasedCards;
  private Set<NobleForm> visitedNobles;
  private Set<NobleForm> reservedNobles;
  private GemsForm gemDiscounts;

  /**
   * Constructor.
   *
   * @param gems           the amount of gems the owned
   * @param reservedCards  the reserved cards
   * @param purchasedCards the purchased cards
   * @param visitedNobles  the owned nobles
   * @param reservedNobles the reserved nobles
   * @param gemDiscounts   the gem discounts
   */
  public HandForm(GemsForm gems, List<CardForm> reservedCards, List<CardForm> purchasedCards,
                  Set<NobleForm> visitedNobles, Set<NobleForm> reservedNobles,
                  GemsForm gemDiscounts) {
    this.gems = gems;
    this.reservedCards = reservedCards;
    this.purchasedCards = purchasedCards;
    this.visitedNobles = visitedNobles;
    this.reservedNobles = reservedNobles;
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
  public List<CardForm> reservedCards() {
    return reservedCards;
  }

  /**
   * A Getter for the Purchased Cards.
   *
   * @return Cards purchased.
   */
  public List<CardForm> purchasedCards() {
    return purchasedCards;
  }

  /**
   * A Getter for the Nobles that are visiting.
   *
   * @return The Nobles that are visiting.
   */
  public Set<NobleForm> visitedNobles() {
    return visitedNobles;
  }

  /**
   * A Getter for the Nobles that have been reserved.
   *
   * @return The Nobles that have been reserved.
   */
  public Set<NobleForm> reservedNobles() {
    return reservedNobles;
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
