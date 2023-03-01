package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.cardform.CardForm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
//import java.util.*;
import javafx.scene.image.Image;


public class Card extends Image {

  public static ArrayList<Card> cards = null;

  // Cost = { CostGreen, CostWhite, CostBlue, CostBlack, CostRed }
  private final int[] aCost;
  private final GemColor aDiscountColor;
  private final int aDiscountAmt;
  private final Expansion aExpansion;
  private final int aLevel;
  private final int aVictoryPoints;

  /**
   * Builds a fully functional card object.
   *
   * @param pCost = cost of card in format { CostGreen, CostWhite, CostBlue, CostBlack, CostRed }
   *              where all costs are integers
   * @param pDiscountColor = color of the card's discount
   * @param pDiscountAmt = number of gems in card's discount
   * @param pExpansion = which expansion the card belongs to
   * @param pLevel = level of the card as an integer
   * @param pVictoryPoints = number of victory points card has
   * @param pFileString = full path of the image file
   */
  public Card(int[] pCost, GemColor pDiscountColor, int pDiscountAmt,
              Expansion pExpansion, int pLevel, int pVictoryPoints, String pFileString) {
    super(pFileString);

    aCost = pCost;
    aDiscountColor = pDiscountColor;
    aDiscountAmt = pDiscountAmt;
    aExpansion = pExpansion;
    aLevel = pLevel;
    aVictoryPoints = pVictoryPoints;
  }

  /**
   * Allows the initialization of the whole deck through a csv file.
   *
   * @post = The ArrayList aInitCards contains a list of every card initialized from CardData.csv
   *
   * future plans: make this take a parameter of a .csv file name, have it return a list of cards
   * so that the OrientExpansion class can handle the actual use of the cards and so the list
   * isn't static
   */
  public static ArrayList<Card> setupCards(String csvFileName) throws IOException {
    cards = new ArrayList<>();

    // Current line of CSV file
    String curLine;

    // Get CardData.csv file
    BufferedReader br = new BufferedReader(new InputStreamReader(
            Objects.requireNonNull(Card.class.getResourceAsStream("images/" + csvFileName))));

    // Skip header of the CSV file
    br.readLine();

    // Read through lines of file and get card data
    while ((curLine = br.readLine()) != null) {
      // Use comma as a delimiter
      String[] cardData = curLine.split(",");

      //System.out.println("Card [Disc color: " + GemColors.valueOf(cardData[5]) + ",
      //Expansion: " + Expansions.valueOf(cardData[7]) + ",
      //File path: images/" + cardData[cardData.length - 1] + "]");

      // Create card
      Card c = new Card(new int[]{Integer.valueOf(cardData[0]), Integer.valueOf(cardData[1]),
              Integer.valueOf(cardData[2]), Integer.valueOf(cardData[3]),
              Integer.valueOf(cardData[4])}, GemColor.valueOf(cardData[5]),
              Integer.valueOf(cardData[6]), Expansion.valueOf(cardData[7]),
              Integer.valueOf(cardData[8]), Integer.valueOf(cardData[9]),
              Card.class.getResource("images/tempcards/" + cardData[10]).toString());

      // Add card to cards list
      cards.add(c);

    }

    return cards;
  }

  public int getLevel() {
    return aLevel;
  }

  public Expansion getExpansion() {
    return aExpansion;
  }

  public int[] getCost() {
    return aCost;
  }
  public GemColor getDiscountColor() {
    return aDiscountColor;
  }
  public int getDiscountAmount() {
    return aDiscountAmt;
  }

  /**
   * Converts card object into text.
   *
   * @return Name of card image
   */
  public String toString() {
    return "\nLevel " + aLevel + " card: [" + Arrays.toString(aCost) + "," + aDiscountColor
            + "," + super.toString() + "]";
  }
}
