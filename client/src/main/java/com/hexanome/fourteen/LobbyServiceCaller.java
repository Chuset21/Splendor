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
}
