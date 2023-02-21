package hexanome.fourteen.server.model.board.tradingposts;

import java.util.HashMap;

/**
 * A mapping of a trading post power to if the player has that power.
 */
public class TradingPosts extends HashMap<TradingPostsEnum, Boolean> {
  /**
   * Constructor.
   */
  public TradingPosts() {
    super();
    this.put(TradingPostsEnum.BONUS_GEM_WITH_CARD, false);
    this.put(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, false);
    this.put(TradingPostsEnum.DOUBLE_GOLD_GEMS, false);
    this.put(TradingPostsEnum.FIVE_PRESETIGE_POINTS, false);
    this.put(TradingPostsEnum.ONE_POINT_PER_POWER, false);
  }
}
