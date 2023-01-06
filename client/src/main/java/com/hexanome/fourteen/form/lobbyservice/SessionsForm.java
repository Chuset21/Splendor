package com.hexanome.fourteen.form.lobbyservice;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.lobbyui.MenuOrganizer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


/**
 * Sessions form.
 */
public final class SessionsForm {

  private Map<String, SessionForm> sessions;

  /**
   * Constructor.
   */
  public SessionsForm(Map<String, SessionForm> sessions) {
    this.sessions = sessions;
  }

  /**
   * No args constructor.
   */
  public SessionsForm() {
  }

  public Map<String, SessionForm> sessions() {
    return sessions;
  }

  /**
   * Finds a session hosted by the given user
   *
   * @param hostName name of session host
   * @return LobbyService ID of the session, which can be used to manage the session through LobbyService.
   * Returns null if no session is found
   */
  public static String getSessionWithHost(String hostName){
    // Get current list of sessions from LobbyService
    SessionsForm sessions = LobbyServiceCaller.getSessions();
    Map<String, SessionForm> sessionMap = sessions.sessions();

    // Iterate until a desired session is found
    for(Map.Entry<String, SessionForm> entry : sessionMap.entrySet()){
      if(entry.getValue().creator().equals(MenuOrganizer.getUsername())){
        return entry.getKey();
      }
    }

    // Or returns null
    return null;
  }
}
