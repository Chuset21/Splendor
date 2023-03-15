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

  /**
   * Returns whether <b>this</b> has at least as many gems
   * of the specific colours and values in gemsNeeded.
   *
   * @param gemsNeeded the gems needed.
   * @return true if <b>this</b> has enough gems, false otherwise
   */
  public boolean hasEnoughGems(Gems gemsNeeded) {
    return gemsNeeded.entrySet().stream()
        .noneMatch(entry -> this.getOrDefault(entry.getKey(), 0) < entry.getValue());
  }

  /**
   * Count the total amount of gems.
   *
   * @return the number of gems.
   */
  public int count() {
    return this.values().stream().mapToInt(value -> value).sum();
  }
}
