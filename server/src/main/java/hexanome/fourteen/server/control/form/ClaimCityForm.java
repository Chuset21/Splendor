package hexanome.fourteen.server.control.form;

import hexanome.fourteen.server.model.board.City;

/**
 * Claim city form.
 */
public final class ClaimCityForm {
  private City cityToClaim;

  /**
   * Constructor.
   *
   * @param cityToClaim The city to claim
   */
  public ClaimCityForm(City cityToClaim) {
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
  public City cityToClaim() {
    return cityToClaim;
  }
}
