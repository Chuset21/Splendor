package hexanome.fourteen.server.model.board.tradingposts;

import java.util.Arrays;
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
    Arrays.stream(TradingPostsEnum.values()).forEach(x -> result.put(x, false));
    return result;
  }
}
