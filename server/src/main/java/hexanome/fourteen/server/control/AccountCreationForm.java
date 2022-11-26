package hexanome.fourteen.server.control;

/**
 * Account creation form.
 */
public class AccountCreationForm {
  private String name;
  private String password;
  private String preferredColour;
  private String role;

  /**
   * No args constructor.
   */
  public AccountCreationForm() {

  }

  public AccountCreationForm(String name, String password) {
    this.name = name;
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public String getPreferredColour() {
    return preferredColour;
  }

  public String getRole() {
    return role;
  }
}
