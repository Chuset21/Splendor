package com.hexanome.fourteen;

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
            .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=").toString(),
        SessionsForm.class);
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
}
