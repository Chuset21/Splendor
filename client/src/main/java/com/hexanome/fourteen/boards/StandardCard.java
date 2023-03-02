package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.cardform.CardLevelForm;
import com.hexanome.fourteen.form.server.cardform.StandardCardForm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.scene.image.Image;

public class StandardCard extends Image {
  private static final Map<StandardCardForm, String> STANDARD_CARD_FORM_MAP = new HashMap<>();

  static {
    try {
      buildMap();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private final StandardCardForm cardForm;

  public StandardCard(StandardCardForm cardForm){

    super(STANDARD_CARD_FORM_MAP.get(cardForm));
    this.cardForm = cardForm;
  }

  private static void buildMap() throws IOException {
    // Get NobleData.csv file
    final BufferedReader br = new BufferedReader(new InputStreamReader(
        Objects.requireNonNull(Card.class.getResourceAsStream("images/StandardCardData.csv"))));

    try {

      // Skip header of the CSV file
      br.readLine();

      // Read through lines of file and get noble data
      String curLine;

      while ((curLine = br.readLine()) != null) {
        // Use comma as a delimiter
        String[] cardData = curLine.split(",");

        // Parse cost
        final GemsForm cost = new GemsForm();

        final int greenCost = Integer.parseInt(cardData[0]);
        cost.computeIfAbsent(GemColor.GREEN, k -> greenCost > 0 ? greenCost : null);

        final int whiteCost = Integer.parseInt(cardData[1]);
        cost.computeIfAbsent(GemColor.WHITE, k -> whiteCost > 0 ? whiteCost : null);

        final int blueCost = Integer.parseInt(cardData[2]);
        cost.computeIfAbsent(GemColor.BLUE, k -> blueCost > 0 ? blueCost : null);

        final int blackCost = Integer.parseInt(cardData[3]);
        cost.computeIfAbsent(GemColor.BLACK, k -> blackCost > 0 ? blackCost : null);

        final int redCost = Integer.parseInt(cardData[4]);
        cost.computeIfAbsent(GemColor.RED, k -> redCost > 0 ? redCost : null);

        final GemColor color = GemColor.STRING_CONVERSION_ARRAY.get(cardData[5]);

        final Expansion expansion = Expansion.CONVERSION_ARRAY.get(cardData[7]);

        final CardLevelForm level = CardLevelForm.CONVERSION_ARRAY.get(Integer.valueOf(cardData[8]));

        final int prestigePoints = Integer.parseInt(cardData[9]);

        STANDARD_CARD_FORM_MAP.put(new StandardCardForm(prestigePoints, cost, level, expansion, color),
            StandardCard.class.getResource("images/cards/" + cardData[10]).toString());
      }
      br.close();
    } catch (Exception e) {
      br.close();
      e.printStackTrace();
    }
  }

  public static void RetrievalTest() {

    GemsForm cost1 = new GemsForm();
    cost1.put(GemColor.WHITE, 4);
    cost1.put(GemColor.BLUE, 2);
    cost1.put(GemColor.BLACK, 1);

    StandardCardForm hash1 = new StandardCardForm(2, cost1, CardLevelForm.TWO, Expansion.STANDARD, GemColor.GREEN);

    String retrieved = STANDARD_CARD_FORM_MAP.get(hash1);

    System.out.println(retrieved);

  }
}
