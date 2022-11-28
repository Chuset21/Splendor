package com.hexanome.fourteen;

import com.hexanome.fourteen.lobbyui.SessionForm;
import com.hexanome.fourteen.lobbyui.SessionsForm;
import kong.unirest.Unirest;

/**
 * Class containing calls to lobby service involving sessions.
 */
public class LobbyServiceCaller {
  /**
   * Private constructor to stop instantiation.
   */
  private LobbyServiceCaller() {
  }

  /**
   * Synchronously get sessions.
   *
   * @return sessions form containing all sessions
   */
  public static SessionsForm getSessions() {
    return Main.GSON.fromJson(Unirest.get("%sapi/sessions".formatted(Main.lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=").asString()
        .getBody(), SessionsForm.class);
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
   * @return the details on a specific session
   */
  public static SessionForm getSessionDetails(String sessionid) {
    return Main.GSON.fromJson(Unirest.get("%sapi/sessions/%s".formatted(Main.lsLocation, sessionid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=").asString()
        .getBody(), SessionForm.class);
  }
}
