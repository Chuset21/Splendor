package com.hexanome.fourteen.form.server;

/**
 * Take gems form.
 */
public final class TakeGemsForm {
  private GemsForm gemsToTake;
  private GemsForm gemsToRemove;

  /**
   * Constructor.
   *
   * @param gemsToTake   The gems to take.
   * @param gemsToRemove The gems to remove (if they have more than 10 gems).
   */
  public TakeGemsForm(GemsForm gemsToTake, GemsForm gemsToRemove) {
    this.gemsToTake = gemsToTake;
    this.gemsToRemove = gemsToRemove;
  }

  /**
   * No args constructor.
   */
  public TakeGemsForm() {

  }

  /**
   * Getter for gems to take.
   *
   * @return gems to take
   */
  public GemsForm gemsToTake() {
    return gemsToTake;
  }

  /**
   * Getter for gems to remove.
   *
   * @return gems to remove
   */
  public GemsForm gemsToRemove() {
    return gemsToRemove;
  }
}

