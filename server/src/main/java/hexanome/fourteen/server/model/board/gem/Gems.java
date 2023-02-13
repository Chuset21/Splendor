package hexanome.fourteen.server.model.board.gem;

import java.util.HashMap;

/**
 * Gems.
 * A mapping of gem colors to an amount of gems.
 */
public final class Gems extends HashMap<GemColor, Integer> {

  /**
   * Constructor.
   */
  public Gems() {
    super();
  }

  /**
   * Produces a shallow copy with the same gem counts.
   *
   * @param gems gems
   */
  public Gems(Gems gems) {
    super(gems);
  }

  public boolean hasEnoughGems(Gems gemsNeeded) {
    return gemsNeeded.entrySet().stream()
        .noneMatch(entry -> this.getOrDefault(entry.getKey(), 0) < entry.getValue());
  }
}
