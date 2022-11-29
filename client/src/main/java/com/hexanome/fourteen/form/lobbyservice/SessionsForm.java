package com.hexanome.fourteen.form.lobbyservice;

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
}
