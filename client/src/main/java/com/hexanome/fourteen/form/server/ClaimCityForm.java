package com.hexanome.fourteen.form.server;

/**
 * Claim city form.
 */
public final class ClaimCityForm {
  private CityForm cityToClaim;

  /**
   * Constructor.
   *
   * @param cityToClaim The city to claim
   */
  public ClaimCityForm(CityForm cityToClaim) {
    this.cityToClaim = cityToClaim;
  }

  /**
   * No args constructor.
   */
  public ClaimCityForm() {

  }

  /**
   * Get the city to claim.
   *
   * @return the city to claim.
   */
  public CityForm cityToClaim() {
    return cityToClaim;
  }
}
