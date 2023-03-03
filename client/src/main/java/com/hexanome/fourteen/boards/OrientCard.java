package com.hexanome.fourteen.boards;

import com.google.gson.reflect.TypeToken;
import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.scene.image.Image;

public class OrientCard extends Image {
  private static final Map<CardForm, String> CARD_FORM_MAP = buildMap();

  public final CardForm cardForm;

  public OrientCard(CardForm cardForm) {
    super(CARD_FORM_MAP.get(cardForm));
    this.cardForm = cardForm;
  }

  private static Map<CardForm, String> buildMap() {
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
}
