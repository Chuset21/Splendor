package com.hexanome.fourteen.boards;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.ImageView;

/**
 * A Class that implements the functionality needed for Player's Hand i.e their cards, gems etc.
 */
public class Hand {


  int prestigePoints;
  List<Card> purchasedCards;
  List<Card> reservedCards;
  public int[] gemDiscounts;
  public int[] gems;


  // TODO:
  // RESERVED NOBLES
  //jokerTokens;

  ImageView purchasedStack;
  ImageView reservedStack;

  /**
   * A Constructor for the Hand.
   */
  public Hand() {
    gems = new int[6];
    gemDiscounts = new int[6];
    prestigePoints = 0;
    purchasedCards = new ArrayList<Card>();
    reservedCards = new ArrayList<Card>();
  }

  /**
   * Allows Player to purchase a Card.
   *
   * @param card The Card the Player wishes to purchase
   */
  public void purchase(Card card) {

    // Get card info
    int[] cost = card.getCost();
    int discountIdx = enumToIndex(card.getDiscountColor());
    int discountAmount = card.getDiscountAmount();

    // Check that we CAN purchase it
    for (int i = 0; i < 5; i++) {
      if (cost[i] > (gems[i] + gemDiscounts[i])) {
        return;
      }
    }

    // Update player's gems
    for (int i = 0; i < 5; i++) {
      // only subtract if (COST-DISCOUNT) is greater than 0,
      // We don't want to be adding gems by accident
      if (cost[i] - gemDiscounts[i] > 0) {
        gems[i] -= (cost[i] - gemDiscounts[i]);
      }
    }

    // Update discounts
    gemDiscounts[discountIdx] = discountAmount;

    // Update cards list
    purchasedCards.add(card);
  }

  /**
   * Allows Player to reserve a Card.
   *
   * @param card The Card the Player wishes to reserve
   */
  public void reserve(Card card) {

    if (reservedCards.size() <= 3) {
      // Update cards list
      reservedCards.add(card);
    }
  }


  private int enumToIndex(GemColor gcolor) {
    switch (gcolor) {
      case GREEN:
        return 0;
      case WHITE:
        return 1;
      case BLUE:
        return 2;
      case BLACK:
        return 3;
      case RED:
        return 4;
      case GOLD:
        return 5;
      default:
        return -1;
    }
  }

}