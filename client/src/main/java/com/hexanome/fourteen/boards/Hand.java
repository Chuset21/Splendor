package com.hexanome.fourteen.boards;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.ImageView;

public class Hand {


  int prestigePoints;
  List<Card> purchasedCards;
  List<Card> reservedCards;
  public int[] gemDiscounts;
  public int[] Gems;


  // TODO:
  // RESERVED NOBLES
  //jokerTokens;

  ImageView purchasedStack;
  ImageView reservedStack;


  public Hand() {
    Gems = new int[6];
    gemDiscounts = new int[6];
    prestigePoints = 0;
    purchasedCards = new ArrayList<Card>();
    reservedCards = new ArrayList<Card>();
  }

  public void purchase(Card card) {

    // Get card info
    int[] cost = card.getCost();
    int dsc_idx = enumToIndex(card.getDiscountColor());
    int dsc_amt = card.getDiscountAmount();

    // Check that we CAN purchase it
    for (int i = 0; i < 6; i++) {
      if (cost[i] > (Gems[i]+gemDiscounts[i])) return;
    }

    // Update player's gems
    for (int i = 0; i < 6; i++) {
      // only subtract if (COST-DISCOUNT) is greater than 0,
      // We don't want to be adding gems by accident
      if (cost[i]-gemDiscounts[i] > 0) {
        Gems[i] -= (cost[i]-gemDiscounts[i]);
      }
    }

    // Update discounts
    gemDiscounts[dsc_idx] = dsc_amt;

    // Update cards list
    purchasedCards.add(card);
  }

  public void reserve(Card card) {

    if (reservedCards.size() <= 3) {
      // Update cards list
      reservedCards.add(card);
    }
  }


  private int enumToIndex(GemColors gcolor) {
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