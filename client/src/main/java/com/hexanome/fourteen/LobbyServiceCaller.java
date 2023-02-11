package com.hexanome.fourteen;

import com.google.gson.reflect.TypeToken;
import com.hexanome.fourteen.form.lobbyservice.CreateSessionForm;
import com.hexanome.fourteen.form.lobbyservice.SaveGameForm;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;
import com.hexanome.fourteen.form.lobbyservice.SessionsForm;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.hexanome.fourteen.lobbyui.MenuController;
import com.hexanome.fourteen.login.LoginResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;

/**
 * Class containing calls to lobby service involving sessions.
 */
public final class LobbyServiceCaller {

  private static String accessToken = "";
  private static String refreshToken = "";
  private static String userid = "";

  /**
   * Private constructor to stop instantiation.
   */
  private LobbyServiceCaller() {
  }

  /**
   * Login a user.
   *
   * @param username username
   * @param password password
   * @return true if successful, false otherwise.
   */
  public static boolean login(String username, String password) {
    HttpResponse<String> response = Unirest.post("%soauth/token".formatted(Main.lsLocation))
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
            .queryString("grant_type", "password")
            .queryString("username", username)
            .queryString("password", password)
            .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
            .asString();
    return getTokens(response);
  }

  /**
   * Update the access token.
   */
  public static boolean updateAccessToken() {
    HttpResponse<String> response = Unirest.post("%soauth/token".formatted(Main.lsLocation))
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
            .queryString("grant_type", "refresh_token")
            .queryString("refresh_token", refreshToken)
            .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
            .asString();
    return getTokens(response);
  }

  private static boolean getTokens(HttpResponse<String> response) {
    if (response.getStatus() != 200) {
      return false;
    }

    final LoginResponse loginResponse =
            Main.GSON.fromJson(response.getBody(), LoginResponse.class);
    accessToken = loginResponse.accessToken();
    refreshToken = loginResponse.refreshToken();
    return true;
  }

  /**
   * Synchronously get sessions.
   *
   * @return Sessions form containing all sessions. Returns null if the request fails.
   */
  public static SessionsForm getSessions() throws TokenRefreshFailedException{
    // Try updating tokens, if fails: send user back to login to refresh tokens
    if (!updateAccessToken()) {
      throw new TokenRefreshFailedException();
    }

    HttpResponse<String> response = Unirest.get("%sapi/sessions".formatted(Main.lsLocation))
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=").asString();

    if (response.getStatus() != 200) {
      return null;
    }

    return Main.GSON.fromJson(response.getBody(), SessionsForm.class);
  }

  /**
   * Join a session.
   *
   * @param player    The player requesting to join
   * @param sessionid The session ID
   * @return true if successful, false otherwise
   */
  public static boolean joinSession(String player, String sessionid) throws TokenRefreshFailedException{
    // Try updating tokens, if fails: send user back to login to refresh tokens
    if (!updateAccessToken()) {
      throw new TokenRefreshFailedException();
    }

    return Unirest.put("%sapi/sessions/%s/players/%s".formatted(Main.lsLocation, sessionid, player))
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
            .queryString("access_token", accessToken).asEmpty().getStatus() == 200;
  }

  /**
   * Leave a session.
   *
   * @param player    The player requesting to leave
   * @param sessionid The session ID
   * @return true if successful, false otherwise
   */
  public static boolean leaveSession(String player, String sessionid) throws TokenRefreshFailedException {
    // Try updating tokens, if fails: send user back to login to refresh tokens
    if (!updateAccessToken()) {
      throw new TokenRefreshFailedException();
    }

    return Unirest.delete("%sapi/sessions/%s/players/%s".formatted(Main.lsLocation, sessionid, player))
                    .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
                    .queryString("access_token", accessToken).asEmpty().getStatus() == 200;
  }

  /**
   * Synchronously get session details.
   *
   * @param sessionid The session ID
   * @return The details on a specific session. Returns null if response fails.
   */
  public static SessionForm getSessionDetails(String sessionid) {
    HttpResponse<String> response =
            Unirest.get("%sapi/sessions/%s".formatted(Main.lsLocation, sessionid))
                    .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=").asString();

    if (response.getStatus() != 200) {
      return null;
    }

    return Main.GSON.fromJson(response.getBody(), SessionForm.class);
  }

  /**
   * Launch a session.
   *
   * @param sessionid The session ID
   * @return true if successful, false otherwise
   */
  public static boolean launchSession(String sessionid) {
    // TODO: Make launchSession work
    HttpResponse<String> response =
            Unirest.post("%sapi/sessions/%s".formatted(Main.lsLocation, sessionid))
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
                    .queryString("access_token", accessToken)
                    .asEmpty();

    return response.getStatus() == 200;
  }

  /**
   * Create a session.
   *
   * @param createSessionForm The form to create a session
   * @return The session ID corresponding to the newly created session or
   * null if the session couldn't be created.
   */
  public static String createSession(CreateSessionForm createSessionForm) throws TokenRefreshFailedException{
    // Try updating tokens, if fails: send user back to login to refresh tokens
    if (!updateAccessToken()) {
      throw new TokenRefreshFailedException();
    }

    HttpResponse<String> response = Unirest.post("%sapi/sessions".formatted(Main.lsLocation))
            .header("Content-Type", "application/json")
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
            .queryString("access_token", accessToken)
            .body(Main.GSON.toJson(createSessionForm))
            .asString();

    if (response.getStatus() != 200) {
      return null;
    }

    return response.getBody();
  }

  /**
   * Deletes a session, requires host's token
   *
   * @param sessionid ID of host's session
   * @return body of HTTP response
   */
  public static boolean deleteSession(String sessionid) {
    return Unirest.delete("%sapi/sessions/%s".formatted(Main.lsLocation, sessionid))
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
            .queryString("access_token", accessToken)
            .asEmpty()
            .getStatus() == 200;
  }

  public static String getAccessToken() {
    return accessToken;
  }

  public static String getRefreshToken() {
    return refreshToken;
  }

  public static String getUserID() {
    return userid;
  }

  public static void setAccessToken(String accessToken) {
    LobbyServiceCaller.accessToken = accessToken;
  }

  public static void setRefreshToken(String refreshToken) {
    LobbyServiceCaller.refreshToken = refreshToken;
  }

  public static void setUserID(String userid) {
    LobbyServiceCaller.userid = userid;
  }



  /**
   * Get saved games.
   *
   * @param authToken The player's token
   * @return The list of saved games
   */
  public static List<SaveGameForm> getSavedGames(
      String authToken) { // Can then use this to filter and see where our player appears
    final Type listType = new TypeToken<ArrayList<SaveGameForm>>() {
    }.getType();
    final List<SaveGameForm> saveGameForms = new ArrayList<>();
    for (GameServiceName e : GameServiceName.values()) {
      final HttpResponse<String> response =
          Unirest.get("%sapi/gameservices/%s/savegames".formatted(Main.lsLocation, e))
              .header("Content-Type", "application/json")
              .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
              .queryString("access_token", authToken).asString();

      if (response.getStatus() == 200) {
        saveGameForms.addAll(Main.GSON.fromJson(response.getBody(), listType));
      }
    }

    return saveGameForms;
  }
}
