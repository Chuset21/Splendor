package com.hexanome.fourteen.form.server;

import java.util.Objects;

/**
 * Class to represent a city.
 */
public final class CityForm {
  private int prestigePoints;
  private GemsForm gemDiscounts;

  /**
   * Construct a city with the required amount of prestige points and gem discounts.
   *
   * @param prestigePoints The required prestige points.
   * @param gemDiscounts   The required gem discounts.
   */
  public CityForm(int prestigePoints, GemsForm gemDiscounts) {
    this.prestigePoints = prestigePoints;
    this.gemDiscounts = gemDiscounts;
  }

  /**
   * No args constructor.
   */
  public CityForm() {

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
  public GemsForm getGemDiscounts() {
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
    CityForm city = (CityForm) o;
    return prestigePoints == city.prestigePoints && Objects.equals(gemDiscounts, city.gemDiscounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, gemDiscounts);
  }
}

