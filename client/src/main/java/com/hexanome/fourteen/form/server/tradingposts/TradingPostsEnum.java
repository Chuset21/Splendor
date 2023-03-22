package com.hexanome.fourteen.form.server.tradingposts;

/**
 * enum to label the trading posts a player may have.
 */
public enum TradingPostsEnum {
  /**
   * After you purchase 1 development card, take 1 gem token.
   */
  BONUS_GEM_WITH_CARD,

  /**
   * When you take 2 gem tokens of the same color, take 1 gem token of another color.
   */
  BONUS_GEM_AFTER_TAKE_TWO,

  /**
   * Each of your gold tokens is worth 2 gem tokens of the same color.
   */
  DOUBLE_GOLD_GEMS,

  /**
   * 5 prestige points.
   */
  FIVE_PRESTIGE_POINTS,

  /**
   * 1 prestige point for each of your coats of arms on the board.
   */
  ONE_POINT_PER_POWER


}

