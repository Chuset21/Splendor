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

  }

  /**
   * Produces a shallow copy with the same gem counts.
   *
   * @param gems gems
   */
  public Gems(Gems gems) {
    super(gems);
  }
}
