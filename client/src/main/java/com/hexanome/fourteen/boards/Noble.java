package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.boards.GemColor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import javafx.scene.image.Image;

/**
 * Noble.
 */
public class Noble extends Image {
  private int prestigePoints;
  private int[] cost;

  /**
   * Constructor.
   *
   * @param prestigePoints The amount of prestige points associated with the noble
   * @param cost           The amount of gem discounts needed to acquire the noble
   */
  public Noble(int[] cost, int prestigePoints, String pFileString) {
    super(pFileString);
    this.cost = cost;
    this.prestigePoints = prestigePoints;
  }

  public static ArrayList<Noble> setupNobles(String csvFileName) throws IOException {
    ArrayList<Noble> nobles = new ArrayList<>();

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
      String[] nobleData = curLine.split(",");

      // Create card
      Noble newNoble = new Noble(new int[] {
          Integer.valueOf(nobleData[0]), Integer.valueOf(nobleData[1]),
          Integer.valueOf(nobleData[2]), Integer.valueOf(nobleData[3]),
          Integer.valueOf(nobleData[4])
      }, Integer.valueOf(nobleData[5]),
          Noble.class.getResource("images/nobles/" + nobleData[6]).toString());

      // Add card to cards list
      nobles.add(newNoble);
    }

    return nobles;
  }

  public int getPrestigePoints() {
    return prestigePoints;
  }

  public int[] getCost() {
    return cost;
  }

  @Override
  public String toString() {
    return "\nPrestige Points: " + getPrestigePoints() + ", Visit Cost: " +
        Arrays.toString(getCost()) + "," + super.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    Noble that = (Noble) obj;
    return this.prestigePoints == that.prestigePoints
        && Objects.equals(this.cost, that.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost);
  }
}