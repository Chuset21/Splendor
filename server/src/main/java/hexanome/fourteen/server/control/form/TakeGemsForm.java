package hexanome.fourteen.server.control.form;

import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Take gems form.
 */
public final class TakeGemsForm {
  private Gems gemsToTake;
  private Gems gemsToRemove;

  /**
   * Constructor.
   *
   * @param gemsToTake   The gems to take.
   * @param gemsToRemove The gems to remove (if they have more than 10 gems).
   */
  public TakeGemsForm(Gems gemsToTake, Gems gemsToRemove) {
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
  public Gems gemsToTake() {
    return gemsToTake;
  }

  /**
   * Getter for gems to remove.
   *
   * @return gems to remove
   */
  public Gems gemsToRemove() {
    return gemsToRemove;
  }
}
