package com.hexanome.fourteen.login;

/**
 * Login Response model.
 */
public final class LoginResponse {
  private String accessToken;
  private transient String tokenType;
  private String refreshToken;
  private int expiresIn;
  private transient String scope;

  /**
   * No args constructor for GSON to use.
   */
  public LoginResponse() {
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
