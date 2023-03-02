package com.hexanome.fourteen.form.server.cardform;

import java.util.ArrayList;
import java.util.Collections;
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

  public static final Map<Integer, CardLevelForm> CONVERSION_ARRAY = new HashMap<>();

  static {
    CONVERSION_ARRAY.put(1, ONE);
    CONVERSION_ARRAY.put(2, TWO);
    CONVERSION_ARRAY.put(3, THREE);
  }
}
