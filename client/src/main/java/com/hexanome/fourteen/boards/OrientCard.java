package com.hexanome.fourteen.boards;

import com.google.gson.reflect.TypeToken;
import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.cardform.CardLevelForm;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.scene.image.Image;

public abstract class OrientCard extends Card {

  public final CardForm cardForm;

  public OrientCard(CardForm cardForm) {
    super(cardForm);
    this.cardForm = cardForm;
  }

  /**
   * @return card level
   */
  @Override
  public int getLevel() {
    return CardLevelForm.TO_INTEGER_CONVERSION_ARRAY.get(cardForm.level()).intValue();
  }

  /**
   * @return card expansion
   */
  @Override
  public Expansion getExpansion() {
    return cardForm.expansion();
  }

  /**
   * @return cost of card
   */
  @Override
  public int[] getCost() {
    return GemsForm.costHashToArray(cardForm.cost());
  }
  @Override
  public abstract GemColor getDiscountColor();

  @Override
  public abstract int getDiscountAmount();
}
