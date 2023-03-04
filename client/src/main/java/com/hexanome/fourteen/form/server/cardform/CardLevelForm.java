package com.hexanome.fourteen.form.server.cardform;

import java.util.HashMap;
import java.util.Map;

/**
 * Card level.
 */
public enum CardLevelForm {
  /**
   * Lowest Card Level.
   */
  ONE,

  /**
   * Middle Card Level.
   */
  TWO,

  /**
   * Highest Card Level.
   */
  THREE;

  public static final Map<Integer, CardLevelForm> TO_ENUM_CONVERSION_ARRAY = new HashMap<>();
  public static final Map<CardLevelForm, Integer> TO_INTEGER_CONVERSION_ARRAY = new HashMap<>();

  static {
    TO_ENUM_CONVERSION_ARRAY.put(1, ONE);
    TO_ENUM_CONVERSION_ARRAY.put(2, TWO);
    TO_ENUM_CONVERSION_ARRAY.put(3, THREE);
  }


  static {
    TO_INTEGER_CONVERSION_ARRAY.put(ONE, 1);
    TO_INTEGER_CONVERSION_ARRAY.put(TWO, 2);
    TO_INTEGER_CONVERSION_ARRAY.put(THREE, 3);
  }
}
