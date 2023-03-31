package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.cardform.GoldGemCardForm;
import com.hexanome.fourteen.form.server.tradingposts.TradingPosts;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Hand form.
 */
public final class HandForm {
  private int prestigePoints;
  private GemsForm gems;
  private List<CardForm> reservedCards;
  private List<CardForm> purchasedCards;
  private Set<NobleForm> visitedNobles;
  private Set<NobleForm> reservedNobles;
  private GemsForm gemDiscounts;
  private TradingPosts tradingPosts;
  private CityForm city;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points the player has
   * @param gems           the amount of gems the owned
   * @param reservedCards  the reserved cards
   * @param purchasedCards the purchased cards
   * @param visitedNobles  the owned nobles
   * @param reservedNobles the reserved nobles
   * @param gemDiscounts   the gem discounts
   * @param tradingPosts   the trading post powers a player has
   * @param city           the city that a player has
   */
  public HandForm(int prestigePoints, GemsForm gems, List<CardForm> reservedCards,
                  List<CardForm> purchasedCards,
                  Set<NobleForm> visitedNobles, Set<NobleForm> reservedNobles,
                  GemsForm gemDiscounts, TradingPosts tradingPosts, CityForm city) {
    this.prestigePoints = prestigePoints;
    this.gems = gems;
    this.reservedCards = reservedCards;
    this.purchasedCards = purchasedCards;
    this.visitedNobles = visitedNobles;
    this.reservedNobles = reservedNobles;
    this.gemDiscounts = gemDiscounts;
    this.tradingPosts = tradingPosts;
    this.city = city;
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
   * A Getter for the Trading Post powers a Player currently has.
   *
   * @return The Trading Post powers a Player currently has.
   */
  public TradingPosts tradingPosts() {
    return tradingPosts;
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

  /**
   * A Getter for the prestige points.
   *
   * @return Player's prestige points
   */
  public int prestigePoints() {
    return prestigePoints;
  }

  /**
   * Getter for city.
   *
   * @return The city that the player owns, null if the player doesn't own one.
   */
  public CityForm city() {
    return city;
  }

  /**
   * Getter for number of gold gem cards
   *
   * @return number of gold gems cards in this hand
   */
  public int getNumGoldGemCards(){
    int numCards = 0;
    for(CardForm c : purchasedCards){
      numCards += c instanceof GoldGemCardForm ? 1 : 0;
    }
    return numCards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HandForm hand = (HandForm) o;
    return prestigePoints == hand.prestigePoints && Objects.equals(gems, hand.gems)
           && Objects.equals(reservedCards, hand.reservedCards)
           && Objects.equals(purchasedCards, hand.purchasedCards)
           && Objects.equals(visitedNobles, hand.visitedNobles)
           && Objects.equals(reservedNobles, hand.reservedNobles)
           && Objects.equals(gemDiscounts, hand.gemDiscounts)
           && Objects.equals(tradingPosts, hand.tradingPosts)
           && Objects.equals(city, hand.city);
  }
}
