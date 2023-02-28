package com.hexanome.fourteen;

import com.google.gson.reflect.TypeToken;
import com.hexanome.fourteen.form.lobbyservice.CreateSessionForm;
import com.hexanome.fourteen.form.lobbyservice.SaveGameForm;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;
import com.hexanome.fourteen.form.lobbyservice.SessionsForm;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.hexanome.fourteen.lobbyui.Lobby;
import com.hexanome.fourteen.lobbyui.User;
import com.hexanome.fourteen.login.LoginResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

/**
 * Class containing calls to lobby service involving sessions.
 */
public final class LobbyServiceCaller {

  private static User currentUser;

  /**
   * Private constructor to stop instantiation.
   */
  private LobbyServiceCaller() {
  }

  /**
   * Gets currentUser's username
   *
   * @return username
   */
  public static String getCurrentUserid(){
    return currentUser.getUserid();
  }

  /**
   * Gets currentUser's lobby
   *
   * @return lobby
   */
  public static Lobby getCurrentUserLobby(){
    return currentUser.getCurrentLobby();
  }

  /**
   * Sets lobby the currentUser is in
   *
   * @param lobby lobby
   */
  public static void setCurrentUserLobby(Lobby lobby) {
    currentUser.setCurrentLobby(lobby);
  }

  /**
   * Login a currentUser. Resets currentUser with new data.
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

    // Resets current user (it's okay if login fails, it will just recreate a new user)
    currentUser = new User(username);

    return getTokens(response);
  }

  /**
   * Update the access token.
   */
  public static boolean updateAccessToken() {
    HttpResponse<String> response = Unirest.post("%soauth/token".formatted(Main.lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("grant_type", "refresh_token")
        .queryString("refresh_token", currentUser.getRefreshToken())
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();

    return getTokens(response);
  }

  /**
   * Parse tokens from request to LobbyService
   * 
   * @param response HTTP response to token refresh request from LS (see updateAccessToken())
   * @return true if tokens are found, false if error is found instead
   */
  private static boolean getTokens(HttpResponse<String> response) {
    if (response.getStatus() != 200) {
      return false;
    }

    final LoginResponse loginResponse =
            Main.GSON.fromJson(response.getBody(), LoginResponse.class);

    currentUser.setAccessToken(loginResponse.accessToken());
    currentUser.setRefreshToken(loginResponse.refreshToken());
    return true;
  }

  /**
   * Gets the current user's username.
   *
   * @return Username if response status is 200, otherwise null.
   */
  public static String getUsername() {

    HttpResponse<String> response = Unirest.get("%soauth/username".formatted(Main.lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", currentUser.getAccessToken())
        .asString();

    if (response.getStatus() != 200) {
      return null;
    }


    return response.getBody();
  }

  /**
   * Synchronously get sessions.
   *
   * @return Sessions form containing all sessions. Returns null if the request fails.
   */
  public static SessionsForm getSessions() {
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
   * @param sessionid The session ID
   * @return true if successful, false otherwise
   */
  public static boolean joinSession(String sessionid) throws TokenRefreshFailedException{
    // Try updating tokens, if fails: send currentUser back to login to refresh tokens
    if (!updateAccessToken()) {
      throw new TokenRefreshFailedException();
    }


    HttpResponse response = Unirest.put("%sapi/sessions/%s/players/%s".formatted(Main.lsLocation, sessionid, currentUser.getUserid()))
            .queryString("access_token", currentUser.getAccessToken())
            .asString();

    return response.getStatus() == 200;
  }

  /**
   * Leave a session.
   *
   * @return true if successful, false otherwise (includes case where currentUser is not in lobby in the first place)
   */
  public static boolean leaveSession() throws TokenRefreshFailedException {
    // Check if currentUser is in a lobby to begin with
    if(currentUser.getCurrentLobby() == null){
      return false;
    }

    // Try updating tokens, if fails: send currentUser back to login to refresh tokens
    if (!updateAccessToken()) {
      throw new TokenRefreshFailedException();
    }

    HttpResponse response = Unirest.delete("%sapi/sessions/%s/players/%s".formatted(Main.lsLocation, currentUser.getCurrentLobby().getSessionid(), currentUser.getUserid()))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", currentUser.getAccessToken())
        .asString();

    return response.getStatus() == 200;
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
   * @return true if successful, false otherwise
   */
  public static boolean launchSession() throws TokenRefreshFailedException{
    // Try updating tokens, if fails: send currentUser back to login to refresh tokens
    if (!updateAccessToken()) {
      throw new TokenRefreshFailedException();
    }

    HttpResponse response =
            Unirest.post("%sapi/sessions/%s".formatted(Main.lsLocation, currentUser.getCurrentLobby().getSessionid()))
                    .queryString("access_token", currentUser.getAccessToken())
                    .asString();

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
    // Try updating tokens, if fails: send currentUser back to login to refresh tokens
    if (!updateAccessToken()) {
      throw new TokenRefreshFailedException();
    }

    HttpResponse<String> response = Unirest.post("%sapi/sessions".formatted(Main.lsLocation))
            .header("Content-Type", "application/json")
            .queryString("access_token", currentUser.getAccessToken())
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
   * @return true if session was deleted, false otherwise (includes case where player wasn't hosting a session)
   */
  public static boolean deleteSession() throws TokenRefreshFailedException {
    // Check if currentUser is in a lobby to begin with
    if(currentUser.getCurrentLobby() == null){
      return false;
    }

    // Try updating tokens, if fails: send currentUser back to login to refresh tokens
    if (!updateAccessToken()) {
      throw new TokenRefreshFailedException();
    }

    HttpResponse response = Unirest.delete("%sapi/sessions/%s".formatted(Main.lsLocation, currentUser.getCurrentLobby().getSessionid()))
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
            .queryString("access_token", currentUser.getAccessToken())
            .asString();

    return response.getStatus() == 200;
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

  /**
   * Get saved games.
   *
   * @return The list of saved games
   */
  public static List<SaveGameForm> getSavedGames() { // Can then use this to filter and see where our player appears
    final Type listType = new TypeToken<ArrayList<SaveGameForm>>() {
    }.getType();
    final List<SaveGameForm> saveGameForms = new ArrayList<>();
    for (GameServiceName e : GameServiceName.values()) {
      final HttpResponse<String> response =
          Unirest.get("%sapi/gameservices/%s/savegames".formatted(Main.lsLocation, e))
              .header("Content-Type", "application/json")
              .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
              .queryString("access_token", currentUser.getAccessToken()).asString();

      if (response.getStatus() == 200) {
        saveGameForms.addAll(Main.GSON.fromJson(response.getBody(), listType));
      }
    }

    return saveGameForms;
  }


  /**
   * Get whether session is active
   *
   * @param session sessionid to check status of
   * @return active status of the given session
   */
  public static boolean isSessionActive(String session){
    SessionsForm sessions = getSessions();
    boolean isActive = false;

    for(Map.Entry<String,SessionForm> entry : sessions.sessions().entrySet()){
      if(entry.getKey().equals(session)){ isActive = true; }
    }

    return isActive;
  }
}
