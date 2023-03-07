package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.GameBoardForm;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.NobleForm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.scene.image.Image;

/**
 * Noble.
 */
public class Noble extends Image {
  private static final Map<NobleForm, String> NOBLE_FORM_MAP = new HashMap<>();

  static {
    try {
      buildMap();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public final NobleForm nobleForm;

  /**
   * Constructor.
   *
   * @param nobleForm form to construct into Noble
   */
  public Noble(NobleForm nobleForm) {
    super(NOBLE_FORM_MAP.get(nobleForm));

    this.nobleForm = nobleForm;
  }

  private static void buildMap() throws IOException {
    // Get NobleData.csv file
    final BufferedReader br = new BufferedReader(new InputStreamReader(
        Objects.requireNonNull(Card.class.getResourceAsStream("images/NobleData.csv"))));

    // Skip header of the CSV file
    br.readLine();
    // Read through lines of file and get noble data
    String curLine;
    while ((curLine = br.readLine()) != null) {
      // Use comma as a delimiter
      String[] nobleData = curLine.split(",");

      final GemsForm cost = new GemsForm();
      final int greenCost = Integer.parseInt(nobleData[0]);
      cost.computeIfAbsent(GemColor.GREEN, k -> greenCost > 0 ? greenCost : null);

      final int whiteCost = Integer.parseInt(nobleData[1]);
      cost.computeIfAbsent(GemColor.WHITE, k -> whiteCost > 0 ? whiteCost : null);

      final int blueCost = Integer.parseInt(nobleData[2]);
      cost.computeIfAbsent(GemColor.BLUE, k -> blueCost > 0 ? blueCost : null);

      final int blackCost = Integer.parseInt(nobleData[3]);
      cost.computeIfAbsent(GemColor.BLACK, k -> blackCost > 0 ? blackCost : null);

      final int redCost = Integer.parseInt(nobleData[4]);
      cost.computeIfAbsent(GemColor.RED, k -> redCost > 0 ? redCost : null);

      final int prestigePoints = Integer.parseInt(nobleData[5]);
      NOBLE_FORM_MAP.put(new NobleForm(prestigePoints, cost), Noble.class.getResource("images/nobles/"+nobleData[6]).toString());
    }
  }

  /**
   * Turns NobleForms from GameBoardForm into list of Nobles
   *
   * @param gameBoardForm form to convert from
   * @return list of Nobles
   */
  public static ArrayList<Noble> interpretNobles(GameBoardForm gameBoardForm) {
    ArrayList<Noble> nobles = new ArrayList<>();

    for (NobleForm nf : gameBoardForm.availableNobles()) {
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
           && Arrays.equals(this.getCost(), that.getCost());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPrestigePoints(), Arrays.hashCode(getCost()));
  }
}