package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.cardform.CardLevelForm;
import java.util.HashMap;
import java.util.Map;

/**
 * An Enumerator that holds all our Expansions.
 */
public enum Expansion {
  STANDARD,
  ORIENT,
  TRADING_POSTS,
  CITIES;

  public static final Map<String, Expansion> CONVERSION_ARRAY = new HashMap<>();

  static {
    CONVERSION_ARRAY.put("STANDARD", STANDARD);
    CONVERSION_ARRAY.put("ORIENT", ORIENT);
    CONVERSION_ARRAY.put("TRADING_POSTS", TRADING_POSTS);
    CONVERSION_ARRAY.put("CITIES", CITIES);
  }
}
