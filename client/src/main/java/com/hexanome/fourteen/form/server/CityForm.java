package com.hexanome.fourteen.form.server;

/**
 * Class to represent a city.
 */
public class CityForm {
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
}

