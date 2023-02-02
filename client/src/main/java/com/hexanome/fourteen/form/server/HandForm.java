package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.form.server.cardform.CardForm;
import java.util.List;
import java.util.Set;

/**
 * Hand form.
 */
public final class HandForm {
  private GemsForm gems;
  private Set<CardForm> reservedCards;
  private Set<CardForm> purchasedCards;
  private List<NobleForm> visitedNobles;
  private NobleForm reservedNoble;
  private GemsForm gemDiscounts;

  /**
   * Constructor.
   *
   * @param gems           the amount of gems the owned
   * @param reservedCards  the reserved cards
   * @param purchasedCards the purchased cards
   * @param visitedNobles   the owned nobles
   * @param reservedNoble  the reserved noble
   * @param gemDiscounts   the gem discounts
   */
  public HandForm(GemsForm gems, Set<CardForm> reservedCards, Set<CardForm> purchasedCards,
                  List<NobleForm> visitedNobles, NobleForm reservedNoble, GemsForm gemDiscounts) {
    this.gems = gems;
    this.reservedCards = reservedCards;
    this.purchasedCards = purchasedCards;
    this.visitedNobles = visitedNobles;
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
   * A Getter for the Nobles that are visiting.
   *
   * @return The Nobles that are visiting.
   */
  public List<NobleForm> visitedNobles() {
    return visitedNobles;
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
