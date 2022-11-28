package com.hexanome.fourteen.form.server;

/**
 * Noble form.
 */
public final class NobleForm {
  private int prestigePoints;
  private GemsForm cost;

  /**
   * Constructor.
   *
   * @param prestigePoints The amount of prestige points associated with the noble
   * @param cost           The amount of gem discounts needed to acquire the noble
   */
  public NobleForm(int prestigePoints, GemsForm cost) {
    this.prestigePoints = prestigePoints;
    this.cost = cost;
  }

  /**
   * No args constructor.
   */
  public NobleForm() {
  }

  public int prestigePoints() {
    return prestigePoints;
  }

  public GemsForm cost() {
    return cost;
  }
}
