package com.hexanome.fourteen.boards;

import javafx.scene.image.Image;

import java.io.*;
import java.util.*;

public class Card extends Image {

  private static final ArrayList<Card> aInitCards = new ArrayList<>();

  // Cost = { CostGreen, CostWhite, CostBlue, CostBlack, CostRed}
  private final int[] aCost;
  private final GemColors aDiscountColor;
  private final int aDiscountAmt;
  private final Expansions aExpansion;
  private final int aLevel;
  private final int aVictoryPoints;

  public Card(int[] pCost, GemColors pDiscountColor, int pDiscountAmt,
              Expansions pExpansion, int pLevel, int pVictoryPoints, String pFileString) {
    super(pFileString);

    aCost = pCost;
    aDiscountColor = pDiscountColor;
    aDiscountAmt = pDiscountAmt;
    aExpansion = pExpansion;
    aLevel = pLevel;
    aVictoryPoints = pVictoryPoints;
  }

  public static ArrayList<Card> getInitCards(){
    return new ArrayList<>(aInitCards);
  }

  public static void setupCards() {
    try {
      // Current line of CSV file
      String curLine;

      // Get CardData.csv file
      BufferedReader br = new BufferedReader(new FileReader("client/target/classes/com/hexanome/fourteen/boards/images/CardData.csv"));

      // Skip header of the CSV file
      br.readLine();

      // Read through lines of file and get card data
      while ((curLine = br.readLine()) != null)
      {
        // Use comma as a delimiter
        String[] cardData = curLine.split(",");

        System.out.println("Card [Disc color: "+GemColors.valueOf(cardData[5])+", Expansion: "+Expansions.valueOf(cardData[7])+", File path: images/"+cardData[cardData.length-1]+"]");

        Card c = new Card(new int[]{Integer.valueOf(cardData[0]),Integer.valueOf(cardData[1]),
                Integer.valueOf(cardData[2]),Integer.valueOf(cardData[3]),Integer.valueOf(cardData[4])},
                GemColors.valueOf(cardData[5]) , Integer.valueOf(cardData[6]), Expansions.valueOf(cardData[7]),
                Integer.valueOf(cardData[8]), Integer.valueOf(cardData[9]), Card.class.getResource("images/"+cardData[10]).toString());
        aInitCards.add(c);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String toString(){
    return "A card with image: "+super.toString();
  }
}
