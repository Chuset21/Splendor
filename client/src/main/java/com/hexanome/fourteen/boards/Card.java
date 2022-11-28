package com.hexanome.fourteen.boards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
//import java.util.*;
import javafx.scene.image.Image;

/**
 * A class that allows us to model all the Card functionality.
 */
public class Card extends Image {

  // Cost = { CostGreen, CostWhite, CostBlue, CostBlack, CostRed, CostYellow }
  private final int[] cost;
  private final GemColor discountColor;
  private final int discountAmt;
  private final Expansion expansions;
  private final int level;
  private final int prestigePoints;

  /**
   * Builds a fully functional card object.
   *
   * @param cost           = cost of card in format { CostGreen, CostWhite, CostBlue, CostBlack,
   *                       CostRed } where all costs are integers
   * @param discountColor  = color of the card's discount
   * @param discountAmt    = number of gems in card's discount
   * @param expansions     = which expansion the card belongs to
   * @param level          = level of the card as an integer
   * @param prestigePoints = number of victory points card has
   * @param fileString     = full path of the image file
   */
  public Card(int[] cost, GemColor discountColor, int discountAmt,
              Expansion expansions, int level, int prestigePoints, String fileString) {
    super(fileString);

    this.cost = cost;
    this.discountColor = discountColor;
    this.discountAmt = discountAmt;
    this.expansions = expansions;
    this.level = level;
    this.prestigePoints = prestigePoints;
  }

  /**
   * Allows the initialization of the whole deck through a csv file.
   *
   * {@code @post} = The ArrayList aInitCards contains a list of every card initialized from
   * CardData.csv
   *
   *     <p>future plans: make this take a parameter of a .csv file name, have it return a list of
   *     cards so that the OrientExpansion class can handle the actual use of the cards and so the
   *     list isn't static
   */
  public static ArrayList<Card> setupCards(String csvFileName) throws IOException {
    ArrayList<Card> cards = new ArrayList<>();

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

      // Add given number of the card type
      for (int i = 0; i < Integer.valueOf(cardData[11]); i++) {

        // Create card
        Card c = new Card(new int[] {Integer.valueOf(cardData[0]), Integer.valueOf(cardData[1]),
            Integer.valueOf(cardData[2]), Integer.valueOf(cardData[3]),
            Integer.valueOf(cardData[4])}, GemColor.valueOf(cardData[5]),
            Integer.valueOf(cardData[6]), Expansion.valueOf(cardData[7]),
            Integer.valueOf(cardData[8]), Integer.valueOf(cardData[9]),
            Card.class.getResource("images/tempcards/" + cardData[10]).toString());

        // Add card to cards list
        cards.add(c);
      }
    }

    return cards;
  }

  public int getLevel() {
    return level;
  }

  public Expansion getExpansions() {
    return expansions;
  }

  public int[] getCost() {
    return cost;
  }

  public GemColor getDiscountColor() {
    return discountColor;
  }

  public int getDiscountAmount() {
    return discountAmt;
  }

  /**
   * Converts card object into text.
   *
   * @return Name of card image
   */
  public String toString() {
    return "\nLevel " + level + " card: [" + Arrays.toString(cost) + "," + discountColor
        + "," + super.toString() + "]";
  }
}
