package com.hexanome.fourteen.lobbyui;

public class User {

  private String accessToken;
  private String refreshToken;
  private final String userid;
  private final String password;
  private Lobby currentLobby;

  public User(String userid, String password) {
    this.userid = userid;
    accessToken = "";
    refreshToken = "";
    currentLobby = null;
    this.password = password;
  }

  public User(String accessToken, String refreshToken, String userid, String password) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.userid = userid;
    this.password = password;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public String getUserid() {
    return userid;
  }

  public Lobby getCurrentLobby() {
    return currentLobby;
  }

  public String getPassword() {
    return password;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public void setCurrentLobby(Lobby currentLobby) {
    this.currentLobby = currentLobby;
  }
}
