package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.Objects;

/**
 * Noble.
 */
public final class Noble {
  private int prestigePoints;
  private Gems cost;

  /**
   * Constructor.
   *
   * @param prestigePoints The amount of prestige points associated with the noble
   * @param cost           The amount of gem discounts needed to acquire the noble
   */
  public Noble(int prestigePoints, Gems cost) {
    this.prestigePoints = prestigePoints;
    this.cost = cost;
  }

  /**
   * No args constructor.
   */
  public Noble() {

  }

  /**
   * Getter for prestige points.
   *
   * @return The prestige points the noble gives the player.
   */
  public int prestigePoints() {
    return prestigePoints;
  }

  /**
   * Getter for cost.
   *
   * @return The cost of the noble.
   */
  public Gems cost() {
    return cost;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    Noble that = (Noble) obj;
    return this.prestigePoints == that.prestigePoints
           && Objects.equals(this.cost, that.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost);
  }
}
