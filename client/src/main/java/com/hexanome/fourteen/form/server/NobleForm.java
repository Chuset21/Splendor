package com.hexanome.fourteen.form.server;

import java.util.Objects;

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

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    NobleForm that = (NobleForm) obj;
    return this.prestigePoints == that.prestigePoints
           && Objects.equals(this.cost, that.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost);
  }
}
