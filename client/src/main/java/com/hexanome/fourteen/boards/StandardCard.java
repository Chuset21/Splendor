package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.cardform.CardLevelForm;
import com.hexanome.fourteen.form.server.cardform.StandardCardForm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.scene.image.Image;

public class StandardCard extends Card {

  private final StandardCardForm cardForm;

  public StandardCard(StandardCardForm cardForm) {
    super(cardForm);

    this.cardForm = cardForm;
  }

  /*public static void RetrievalTest() {

    GemsForm cost1 = new GemsForm();
    cost1.put(GemColor.WHITE, 4);
    cost1.put(GemColor.BLUE, 2);
    cost1.put(GemColor.BLACK, 1);

    StandardCardForm hash1 = new StandardCardForm(2, cost1, CardLevelForm.TWO, Expansion.STANDARD, GemColor.GREEN);

    String retrieved = STANDARD_CARD_FORM_MAP.get(hash1);

    System.out.println(retrieved);

  }*/

  /**
   * @return card level
   */
  @Override
  public int getLevel() {
    return CardLevelForm.TO_INTEGER_CONVERSION_ARRAY.get(cardForm.level());
  }

  /**
   * @return card expansion
   */
  @Override
  public Expansion getExpansion() {
    return cardForm.expansion();
  }

  /**
   * @return cost array
   */
  @Override
  public int[] getCost() {
    return GemsForm.costHashToArray(cardForm.cost());
  }

  /**
   * @return
   */
  @Override
  public GemColor getDiscountColor() {
    return cardForm.discountColor();
  }

  /**
   * @return
   */
  @Override
  public int getDiscountAmount() {
    return 1;
  }

  @Override
  public CardForm getCardForm(){
    return cardForm;
  }

  /**
   * Converts card object into text.
   *
   * @return Name of card image
   */
  public String toString() {
    return "\nStandardCard, Level " +
           CardLevelForm.TO_INTEGER_CONVERSION_ARRAY.get(cardForm.level())
           + " card: [" + Arrays.toString(GemsForm.costHashToArray(cardForm.cost())) + "," +
           cardForm.discountColor()
           + "]";
  }
}
