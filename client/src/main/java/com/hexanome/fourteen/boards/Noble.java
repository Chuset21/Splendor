package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.GameBoardForm;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.NobleForm;
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
  private NobleForm nobleForm;

  /**
   * Constructor.
   *
   * @param nobleForm form to construct into Noble
   */
  private Noble(NobleForm nobleForm) throws IOException {
    super(getImageFileFromCSV(nobleForm));

    this.nobleForm = nobleForm;
  }

  private static String getImageFileFromCSV(NobleForm nobleForm) throws IOException{
    String csvFileName = "NobleData.csv";

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

      int[] cost = {Integer.valueOf(nobleData[0]), Integer.valueOf(nobleData[1]),
          Integer.valueOf(nobleData[2]), Integer.valueOf(nobleData[3]),
          Integer.valueOf(nobleData[4])};
      int prestigePoints = Integer.valueOf(nobleData[5]);

      // If nobleForm has equivalent data to CSV entry
      if(Arrays.equals(GemsForm.costHashToArray(nobleForm.cost()), cost)
          && nobleForm.prestigePoints() == prestigePoints){
        // Add card to cards list
        return Noble.class.getResource("images/nobles/" + nobleData[6]).toString();
      }
      // Otherwise, loop
    }

    throw new IOException("No matching noble found in "+csvFileName);
  }

  public static ArrayList<Noble> interpretNobles(GameBoardForm gameBoardForm) throws IOException {
    ArrayList<Noble> nobles = new ArrayList<>();

    for(NobleForm nf : gameBoardForm.availableNobles()){
      nobles.add(new Noble(nf));
    }

    return nobles;
  }

  public int getPrestigePoints() {
    return nobleForm.prestigePoints();
  }

  public int[] getCost() {
    return GemsForm.costHashToArray(nobleForm.cost());
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
    return this.getPrestigePoints() == that.getPrestigePoints()
        && Objects.equals(this.getCost(), that.getCost());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPrestigePoints(), getCost());
  }
}