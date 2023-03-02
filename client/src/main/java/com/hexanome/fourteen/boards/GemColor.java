package com.hexanome.fourteen.boards;

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

  public static final Map<String, GemColor> STRING_CONVERSION_ARRAY = new HashMap<>();
  public static final Map<Integer, GemColor> INT_CONVERSION_ARRAY = new HashMap<>();

  static {
    STRING_CONVERSION_ARRAY.put("GREEN", GREEN);
    STRING_CONVERSION_ARRAY.put("WHITE", WHITE);
    STRING_CONVERSION_ARRAY.put("BLUE", BLUE);
    STRING_CONVERSION_ARRAY.put("BLACK", BLACK);
    STRING_CONVERSION_ARRAY.put("RED", RED);
    STRING_CONVERSION_ARRAY.put("GOLD", GOLD);

    INT_CONVERSION_ARRAY.put(0, GREEN);
    INT_CONVERSION_ARRAY.put(1, WHITE);
    INT_CONVERSION_ARRAY.put(2, BLUE);
    INT_CONVERSION_ARRAY.put(3, BLACK);
    INT_CONVERSION_ARRAY.put(4, RED);
    INT_CONVERSION_ARRAY.put(5, GOLD);
  }


}
