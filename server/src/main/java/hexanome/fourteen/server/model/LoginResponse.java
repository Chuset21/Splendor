package hexanome.fourteen.server.model;

/**
 * Login Response model.
 */
public final class LoginResponse {
  private final String accessToken;
  private final transient String tokenType;
  private final String refreshToken;
  private final transient int expiresIn;
  private final transient String scope;

  public LoginResponse(String accessToken, String tokenType, String refreshToken,
                       int expiresIn, String scope) {
    this.accessToken = accessToken;
    this.tokenType = tokenType;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.scope = scope;
  }

  public String accessToken() {
    return accessToken;
  }

  public String tokenType() {
    return tokenType;
  }

  public String refreshToken() {
    return refreshToken;
  }

  public int expiresIn() {
    return expiresIn;
  }

  public String scope() {
    return scope;
  }
}