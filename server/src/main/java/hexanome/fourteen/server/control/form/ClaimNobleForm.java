package hexanome.fourteen.server.control.form;

import hexanome.fourteen.server.model.board.Noble;

/**
 * Claim noble form.
 */
public final class ClaimNobleForm {
  private Noble nobleToClaim;

  /**
   * Constructor.
   *
   * @param nobleToClaim The noble to claim
   */
  public ClaimNobleForm(Noble nobleToClaim) {
    this.nobleToClaim = nobleToClaim;
  }

  /**
   * No args constructor.
   */
  public ClaimNobleForm() {

  }

  /**
   * Get the noble to claim.
   *
   * @return the noble to claim.
   */
  public Noble nobleToClaim() {
    return nobleToClaim;
  }
}
