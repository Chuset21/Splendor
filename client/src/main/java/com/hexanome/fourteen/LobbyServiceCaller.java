package com.hexanome.fourteen;

import com.hexanome.fourteen.form.lobbyservice.CreateSessionForm;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;
import com.hexanome.fourteen.form.lobbyservice.SessionsForm;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

/**
 * Class containing calls to lobby service involving sessions.
 */
public final class LobbyServiceCaller {
  /**
   * Private constructor to stop instantiation.
   */
  private LobbyServiceCaller() {
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
   * @param player    The player requesting to join
   * @param authtoken The player's token
   * @param sessionid The session ID
   * @return true if successful, false otherwise
   */
  public static boolean joinSession(String player, String authtoken, String sessionid) {
    return Unirest.put("%sapi/sessions/%s/players/%s".formatted(Main.lsLocation, sessionid, player))
               .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
               .queryString("access_token", authtoken).asEmpty().getStatus() == 200;
  }

  /**
   * Leave a session.
   *
   * @param player    The player requesting to leave
   * @param authtoken The player's token
   * @param sessionid The session ID
   * @return true if successful, false otherwise
   */
  public static boolean leaveSession(String player, String authtoken, String sessionid) {
    return
        Unirest.delete("%sapi/sessions/%s/players/%s".formatted(Main.lsLocation, sessionid, player))
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
            .queryString("access_token", authtoken).asEmpty().getStatus() == 200;
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
    return Unirest.post("%sapi/sessions/%s".formatted(Main.lsLocation, sessionid))
               .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=").asEmpty()
               .getStatus() == 200;
  }

  /**
   * Create a session.
   *
   * @param authtoken         The player's token
   * @param createSessionForm The form to create a session
   * @return The session ID corresponding to the newly created session.
   *     Returns null if the session couldn't be created
   */
  public static String createSession(String authtoken, CreateSessionForm createSessionForm) {
    HttpResponse<String> response = Unirest.post("%sapi/sessions".formatted(Main.lsLocation))
        .header("Content-Type", "application/json")
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", authtoken).body(Main.GSON.toJson(createSessionForm))
        .asString();

    if (response.getStatus() != 200) {
      return null;
    }

    return response.getBody();
  }
}
