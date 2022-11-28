package hexanome.fourteen.server.control;

import com.google.gson.annotations.SerializedName;

/**
 * Account creation form.
 */
public final class AccountCreationForm {
  private String name;
  private String password;
  @SerializedName("preferredColour")
  private String preferredColour;
  private String role;

  /**
   * No args constructor.
   */
  public AccountCreationForm() {
  }

  /**
   * Constructor.
   *
   * @param name     Name
   * @param password Password
   */
  public AccountCreationForm(String name, String password) {
    this.name = name;
    this.password = password;
    preferredColour = "01FFFF";
    role = "ROLE_SERVICE";
  }

  /**
   * A Getter to get the Name.
   *
   * @return Name associated with the account
   */
  public String getName() {
    return name;
  }

  /**
   * A Getter to get the Password.
   *
   * @return Password associated with the account
   */
  public String getPassword() {
    return password;
  }

  /**
   * A Getter to get the PreferredColour.
   *
   * @return PreferredColour associated with the account
   */
  public String getPreferredColour() {
    return preferredColour;
  }

  /**
   * A Getter to get the Role.
   *
   * @return Role associated with the account
   */
  public String getRole() {
    return role;
  }
}
