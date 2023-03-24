package com.hexanome.fourteen.boards;

import com.google.gson.reflect.TypeToken;
import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.cardform.CardLevelForm;
import com.hexanome.fourteen.form.server.cardform.DoubleBonusCardForm;
import com.hexanome.fourteen.form.server.cardform.GoldGemCardForm;
import com.hexanome.fourteen.form.server.cardform.ReserveNobleCardForm;
import com.hexanome.fourteen.form.server.cardform.SacrificeCardForm;
import com.hexanome.fourteen.form.server.cardform.SatchelCardForm;
import com.hexanome.fourteen.form.server.cardform.StandardCardForm;
import com.hexanome.fourteen.form.server.cardform.WaterfallCardForm;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.scene.image.Image;

public class OrientCard extends Card {

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
   * @return cost of card
   */
  @Override
  public int[] getCost() {
    return GemsForm.costHashToArray(cardForm.cost());
  }

  @Override
  public GemColor getDiscountColor() {
    if (cardForm instanceof DoubleBonusCardForm c) {
      return c.discountColor();
    } else if (cardForm instanceof ReserveNobleCardForm c) {
      return c.discountColor();
    } else if (cardForm instanceof SacrificeCardForm c) {
      return c.discountColor();
    } else if (cardForm instanceof WaterfallCardForm c) {
      return c.discountColor();
    } else if (cardForm instanceof SatchelCardForm c) {
      if (c.cardToAttach() == null) {
        return null;
      } else if (c.cardToAttach() instanceof DoubleBonusCardForm d) {
        return d.discountColor();
      } else if (c.cardToAttach() instanceof ReserveNobleCardForm d) {
        return d.discountColor();
      } else if (c.cardToAttach() instanceof SacrificeCardForm d) {
        return d.discountColor();
      } else if (c.cardToAttach() instanceof WaterfallCardForm d) {
        return d.discountColor();
      } else if (c.cardToAttach() instanceof StandardCardForm d) {
        return d.discountColor();
      }
      return null;
    }
    return null;
  }

  @Override
  public CardForm getCardForm() {
    return cardForm;
  }

  @Override
  public int getDiscountAmount() {
    return (cardForm instanceof DoubleBonusCardForm || cardForm instanceof SatchelCardForm) ? 2 : 1;
  }

  @Override
  public String toString() {
    return Card.ORIENT_CARD_FORM_MAP.get(cardForm);
  }
}
