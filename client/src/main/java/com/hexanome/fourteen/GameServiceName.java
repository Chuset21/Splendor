package com.hexanome.fourteen;

import com.hexanome.fourteen.boards.Expansion;
import java.util.Set;

/**
 * Game service name.
 */
public enum GameServiceName {
  ALL, BASE, CITIES, TRADING_POSTS;

  /**
   * Get the expansions from the game service name.
   *
   * @param gameServiceName game service name.
   * @return The expansions.
   */
  public static Set<Expansion> getExpansions(GameServiceName gameServiceName) {
    return switch (gameServiceName) {
      case ALL ->
          Set.of(Expansion.TRADING_POSTS, Expansion.CITIES, Expansion.ORIENT, Expansion.STANDARD);
      case BASE -> Set.of(Expansion.ORIENT, Expansion.STANDARD);
      case CITIES -> Set.of(Expansion.CITIES, Expansion.ORIENT, Expansion.STANDARD);
      case TRADING_POSTS -> Set.of(Expansion.TRADING_POSTS, Expansion.ORIENT, Expansion.STANDARD);
    };
  }

  /**
   * Get game service name from expansions.
   *
   * @param expansions expansions
   * @return The game service name.
   */
  public static GameServiceName getGameServiceName(Set<Expansion> expansions) {
    if (expansions.contains(Expansion.CITIES)) {
      if (expansions.contains(Expansion.TRADING_POSTS)) {
        return ALL;
      } else {
        return CITIES;
      }
    } else if (expansions.contains(Expansion.TRADING_POSTS)) {
      return TRADING_POSTS;
    } else {
      return BASE;
    }
  }
}
