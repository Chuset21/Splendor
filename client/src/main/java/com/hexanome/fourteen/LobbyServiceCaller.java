package com.hexanome.fourteen;

import com.google.gson.reflect.TypeToken;
import com.hexanome.fourteen.form.lobbyservice.CreateSessionForm;
import com.hexanome.fourteen.form.lobbyservice.SaveGameForm;
import com.hexanome.fourteen.form.lobbyservice.SessionsForm;
import com.hexanome.fourteen.lobbyui.Lobby;
import com.hexanome.fourteen.lobbyui.User;
import com.hexanome.fourteen.login.LoginResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
  public static String getCurrentUserid() {
    return currentUser.getUserid();
  }

  /**
   * Gets currentUser's lobby
   *
   * @return lobby
   */
  public static Lobby getCurrentUserLobby() {
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
   * Gets access token for game service (to delete sessions)
   *
   * @return token of the game service
   */
  private static String getGameServiceToken() {
    HttpResponse<String> response = Unirest.post("%soauth/token".formatted(Main.lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("grant_type", "password")
        .queryString("username", "Splendor")
        .queryString("password", "abc123_ABC123")
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();

    if (!response.isSuccess()) {
      return null;
    }

    final LoginResponse loginResponse =
        Main.GSON.fromJson(response.getBody(), LoginResponse.class);

    return loginResponse.accessToken();
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
    if (response.isSuccess() && (currentUser == null || !(currentUser.getUserid().equals(username) && currentUser.getPassword().equals(password)))) {
      currentUser = new User(username, password);
    }

    return getTokens(response);
  }

  /**
   * Update the access token.
   */
  private static boolean updateAccessToken() {
    HttpResponse<String> response = Unirest.post("%soauth/token".formatted(Main.lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("grant_type", "refresh_token")
        .queryString("refresh_token", currentUser.getRefreshToken())
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();

    return getTokens(response);
  }

  /**
   * Updates the user's access token and sends it in the return.
   */
  public static String getCurrentUserAccessToken() {
    HttpResponse<String> response = Unirest.post("%soauth/token".formatted(Main.lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("grant_type", "password")
        .queryString("username", currentUser.getUserid())
        .queryString("password", currentUser.getPassword())
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();

    getTokens(response);

    return currentUser.getAccessToken();
  }

  /**
   * Parse tokens from request to LobbyService.
   *
   * @param response HTTP response to token refresh request from LS (see updateAccessToken())
   * @return true if tokens are found, false if error is found instead
   */
  private static boolean getTokens(HttpResponse<String> response) {
    if (!response.isSuccess()) {
      if (currentUser != null && !login(currentUser.getUserid(), currentUser.getPassword())) {
        return false;
      }
    }

    final LoginResponse loginResponse =
        Main.GSON.fromJson(response.getBody(), LoginResponse.class);
    currentUser.setAccessToken(loginResponse.accessToken());
    currentUser.setRefreshToken(loginResponse.refreshToken());
    return true;
  }

  /**
   * Synchronously get sessions.
   *
   * @return Http response containing session form which contains all sessions.
   */
  public static HttpResponse<String> getSessions() {
    return Unirest.get("%sapi/sessions".formatted(Main.lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .asString();
  }

  /**
   * Asynchronously get sessions.
   *
   * @param hash The hash of the previous response body
   * @return Http response containing session form which contains all sessions.
   */
  public static HttpResponse<String> getSessions(String hash) {
    return Unirest.get("%sapi/sessions".formatted(Main.lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("hash", hash == null || hash.isBlank() ? "" : hash).asString();
  }

  /**
   * Join a session.
   *
   * @param sessionid The session ID
   * @return true if successful, false otherwise
   */
  public static boolean joinSession(String sessionid) {
    HttpResponse<String> response = Unirest.put(
            "%sapi/sessions/%s/players/%s".formatted(Main.lsLocation, sessionid,
                currentUser.getUserid()))
        .queryString("access_token", getCurrentUserAccessToken())
        .asString();

    return response.isSuccess();
  }

  /**
   * Leave a session.
   *
   * @return true if successful, false otherwise (includes case where currentUser is not in lobby in the first place)
   */
  public static boolean leaveSession() {
    // Check if currentUser is in a lobby to begin with
    if (currentUser.getCurrentLobby() == null) {
      return false;
    }

    HttpResponse<String> response =
        Unirest.delete("%sapi/sessions/%s/players/%s".formatted(Main.lsLocation,
                currentUser.getCurrentLobby().getSessionid(), currentUser.getUserid()))
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
            .queryString("access_token", getCurrentUserAccessToken())
            .asString();

    return response.isSuccess();
  }

  /**
   * Synchronously get session details.
   *
   * @param sessionid The session ID
   * @return The details on a specific session.
   */
  public static HttpResponse<String> getSessionDetails(String sessionid) {
    return Unirest.get("%sapi/sessions/%s".formatted(Main.lsLocation, sessionid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .asString();
  }

  /**
   * Asynchronously get session details.
   *
   * @param sessionid The session ID
   * @param hash      The hash of the previous response body
   * @return The details on a specific session.
   */
  public static HttpResponse<String> getSessionDetails(String sessionid, String hash) {
    return Unirest.get("%sapi/sessions/%s".formatted(Main.lsLocation, sessionid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("hash", hash == null || hash.isBlank() ? "" : hash).asString();
  }

  /**
   * Launch a session.
   *
   * @return true if successful, false otherwise
   */
  public static boolean launchSession() {
    HttpResponse<String> response =
        Unirest.post("%sapi/sessions/%s".formatted(Main.lsLocation,
                currentUser.getCurrentLobby().getSessionid()))
            .queryString("access_token", getCurrentUserAccessToken())
            .asString();

    return response.isSuccess();
  }

  /**
   * Launch a session.
   *
   * @param sessionid session to launch
   * @return true if successful, false otherwise
   */
  public static boolean launchSession(String sessionid) {
    HttpResponse<String> response =
        Unirest.post("%sapi/sessions/%s".formatted(Main.lsLocation, sessionid))
            .queryString("access_token", getCurrentUserAccessToken())
            .asString();

    return response.isSuccess();
  }

  /**
   * Create a session.
   *
   * @param createSessionForm The form to create a session
   * @return The session ID corresponding to the newly created session or
   * null if the session couldn't be created.
   */
  public static String createSession(CreateSessionForm createSessionForm) {
    HttpResponse<String> response = Unirest.post("%sapi/sessions".formatted(Main.lsLocation))
        .header("Content-Type", "application/json")
        .queryString("access_token", getCurrentUserAccessToken())
        .body(Main.GSON.toJson(createSessionForm))
        .asString();

    if (!response.isSuccess()) {
      return null;
    }

    return response.getBody();
  }

  /**
   * Deletes a session from LobbyService, DON'T USE UNLESS NECESSARY
   *
   * @return true if session was deleted, false otherwise (includes case where player wasn't hosting a session)
   */
  public static boolean deleteSession() {
    // Check if currentUser is in a lobby to begin with
    if (currentUser.getCurrentLobby() == null) {
      return false;
    }

    HttpResponse<String> response = Unirest.delete("%sapi/sessions/%s".formatted(Main.lsLocation,
            currentUser.getCurrentLobby().getSessionid()))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", getCurrentUserAccessToken())
        .asString();

    return response.isSuccess();
  }

  /**
   * Deletes a session from LobbyService, DON'T USE UNLESS NECESSARY
   *
   * @return true if session was deleted, false otherwise (includes case where player wasn't hosting a session)
   */
  public static boolean deleteLaunchedSession() throws TokenRefreshFailedException {
    // Check if currentUser is in a lobby to begin with
    if (currentUser.getCurrentLobby() == null
        || !currentUser.getCurrentLobby().getHost().equals(currentUser.getUserid())) {
      return false;
    }

    String gsToken = getGameServiceToken();

    // Try updating tokens, if fails: send currentUser back to login to refresh tokens
    if (gsToken == null) {
      throw new TokenRefreshFailedException();
    }

    HttpResponse<String> response = Unirest.delete("%sapi/sessions/%s".formatted(Main.lsLocation,
            currentUser.getCurrentLobby().getSessionid()))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", gsToken)
        .asString();

    return response.isSuccess();
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

      if (response.isSuccess()) {
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
  public static List<SaveGameForm> getSavedGames() {
    return getSavedGames(getCurrentUserAccessToken());
  }


  /**
   * Get whether session is active.
   *
   * @param session sessionid to check status of
   * @return active status of the given session
   */
  public static boolean isSessionActive(String session) {
    final SessionsForm sessions =
        Main.GSON.fromJson(LobbyServiceCaller.getSessions().getBody(), SessionsForm.class);
    if (sessions == null) {
      return false;
    }

    return sessions.sessions().keySet().stream().anyMatch(k -> k.equals(session));
  }
}
