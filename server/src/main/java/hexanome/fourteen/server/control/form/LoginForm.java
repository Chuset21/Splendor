package hexanome.fourteen.server.control.form;

/**
 * Login Response model.
 */
public final class LoginForm {
  private String accessToken;
  private transient String tokenType;
  private String refreshToken;
  private transient int expiresIn;
  private transient String scope;

  /**
   * No args constructor for GSON to use.
   */
  public LoginForm() {
  }

  /**
   * A Getter for the Access Token.
   *
   * @return The Access Token
   */
  public String accessToken() {
    return accessToken;
  }

  /**
   * A Getter for the Token Type.
   *
   * @return The Token Type
   */
  public String tokenType() {
    return tokenType;
  }

  /**
   * A Getter for the Refresh Token.
   *
   * @return The Refresh Token
   */
  public String refreshToken() {
    return refreshToken;
  }

  /**
   * A Getter for when the Token expires.
   *
   * @return The expiry time of Token
   */
  public int expiresIn() {
    return expiresIn;
  }

  /**
   * A Getter for the Scope.
   *
   * @return The Scope
   */
  public String scope() {
    return scope;
  }
}