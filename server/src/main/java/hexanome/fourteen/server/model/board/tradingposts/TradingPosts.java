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
  }

  /**
   * Default creation of trading posts.
   *
   * @return A trading posts object initialised with the default values.
   */
  public static TradingPosts createDefault() {
    final TradingPosts result = new TradingPosts();
    result.put(TradingPostsEnum.BONUS_GEM_WITH_CARD, false);
    result.put(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, false);
    result.put(TradingPostsEnum.DOUBLE_GOLD_GEMS, false);
    result.put(TradingPostsEnum.FIVE_PRESTIGE_POINTS, false);
    result.put(TradingPostsEnum.ONE_POINT_PER_POWER, false);
    return result;
  }
}
