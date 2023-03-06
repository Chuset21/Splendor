package com.hexanome.fourteen.boards;

import com.google.gson.reflect.TypeToken;
import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.cardform.CardLevelForm;
import com.hexanome.fourteen.form.server.cardform.StandardCardForm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.scene.image.Image;


public abstract class Card extends Image {

  private static final Map<StandardCardForm, String> STANDARD_CARD_FORM_MAP = new HashMap<>();
  public static final Map<CardForm, String> ORIENT_CARD_FORM_MAP = buildOrientMap();

  static {
    try {
      buildStandardMap();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Card(CardForm cardForm){
    super((cardForm instanceof StandardCardForm) ? STANDARD_CARD_FORM_MAP.get((StandardCardForm) cardForm) : ORIENT_CARD_FORM_MAP.get(cardForm));
  }

  private static void buildStandardMap() throws IOException {
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

        final CardLevelForm level = CardLevelForm.TO_ENUM_CONVERSION_ARRAY.get(Integer.valueOf(cardData[8]));

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

  private static Map<CardForm, String> buildOrientMap() {
    final Type mapType = new TypeToken<Map<String, CardForm>>() {
    }.getType();
    final Map<String, CardForm> map = Main.GSON.fromJson(new Scanner(
        Objects.requireNonNull(OrientCard.class.getResourceAsStream("images/OrientCardData.json")),
        StandardCharsets.UTF_8).useDelimiter("\\A").next(), mapType);

    return map.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getValue,
            e -> Objects.requireNonNull(
                OrientCard.class.getResource("images/cards/" + e.getKey())).toString()));
  }

  public abstract int getLevel();
  public abstract Expansion getExpansion();
  public abstract int[] getCost();
  public abstract GemColor getDiscountColor();
  public abstract int getDiscountAmount();
  public abstract CardForm getCardForm();

  /**
   * Converts card object into text.
   *
   * @return Name of card image
   */
  public abstract String toString();
}
