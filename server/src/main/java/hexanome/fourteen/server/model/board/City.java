package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.Objects;

/**
 * Class to represent a city.
 */
public final class City {
  private int prestigePoints;
  private Gems gemDiscounts;

  /**
   * Construct a city with the required amount of prestige points and gem discounts.
   *
   * @param prestigePoints The required prestige points.
   * @param gemDiscounts   The required gem discounts.
   */
  public City(int prestigePoints, Gems gemDiscounts) {
    this.prestigePoints = prestigePoints;
    this.gemDiscounts = gemDiscounts;
  }

  /**
   * No args constructor.
   */
  public City() {

  }

  /**
   * Getter for required prestige points.
   *
   * @return Required prestige points.
   */
  public int getPrestigePoints() {
    return prestigePoints;
  }

  /**
   * Getter for required gem discounts.
   * Gold gem discounts mean any gem can fulfill that role.
   *
   * @return Required gem discounts.
   */
  public Gems getGemDiscounts() {
    return gemDiscounts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    City city = (City) o;
    return prestigePoints == city.prestigePoints && Objects.equals(gemDiscounts, city.gemDiscounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, gemDiscounts);
  }
}
