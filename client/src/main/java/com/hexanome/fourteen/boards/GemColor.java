package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.cardform.CardLevelForm;
import java.util.HashMap;
import java.util.Map;

/**
 * An Enumerator that holds all our Gem Colors.
 */
public enum GemColor {
  GREEN,
  WHITE,
  BLUE,
  BLACK,
  RED,
  GOLD;

  public static final Map<String, GemColor> CONVERSION_ARRAY = new HashMap<>();

  static {
    CONVERSION_ARRAY.put("GREEN", GREEN);
    CONVERSION_ARRAY.put("WHITE", WHITE);
    CONVERSION_ARRAY.put("BLUE", BLUE);
    CONVERSION_ARRAY.put("BLACK", BLACK);
    CONVERSION_ARRAY.put("RED", RED);
    CONVERSION_ARRAY.put("GOLD", GOLD);
  }
}
