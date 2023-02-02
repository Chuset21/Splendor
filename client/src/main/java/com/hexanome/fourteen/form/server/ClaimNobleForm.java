package com.hexanome.fourteen.form.server;

/**
 * Claim noble form.
 */
public final class ClaimNobleForm {
  private NobleForm nobleToClaim;

  /**
   * Constructor.
   *
   * @param nobleToClaim The noble to claim
   */
  public ClaimNobleForm(NobleForm nobleToClaim) {
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
  public NobleForm nobleToClaim() {
    return nobleToClaim;
  }
}

