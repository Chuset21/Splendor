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
}
