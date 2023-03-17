package hexanome.fourteen.server.control.form.tradingpost;

import hexanome.fourteen.server.model.board.gem.GemColor;

/**
 * Class to represent taking an extra gem due to power 1.
 */
public final class TradingPostTakeGem {
  private GemColor gemToTake;
  private GemColor gemToRemove;

  /**
   * No args constructor.
   */
  public TradingPostTakeGem() {

  }

  /**
   * Constructor.
   *
   * @param gemToTake   The gem to take.
   * @param gemToRemove The gem to remove,
   *                    in the case that you have more than 10 gems at the end of your turn.
   */
  public TradingPostTakeGem(GemColor gemToTake, GemColor gemToRemove) {
    this.gemToTake = gemToTake;
    this.gemToRemove = gemToRemove;
  }

  /**
   * Getter for gem to take.
   *
   * @return The gem to take.
   */
  public GemColor gemToTake() {
    return gemToTake;
  }

  /**
   * Getter for gem to remove.
   *
   * @return The gem to remove, null if no gem must be removed.
   */
  public GemColor gemToRemove() {
    return gemToRemove;
  }
}
