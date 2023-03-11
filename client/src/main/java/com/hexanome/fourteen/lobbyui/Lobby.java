package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.form.lobbyservice.GameParametersForm;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;
import kong.unirest.HttpResponse;
import org.apache.commons.codec.digest.DigestUtils;

public class Lobby {

  private String sessionid;
  private SessionForm session;
  private Expansion expansion;

  public Lobby(String sessionid) {

    // Get settings for session
    final HttpResponse<String> response = LobbyServiceCaller.getSessionDetails(sessionid);
    session = Main.GSON.fromJson(response.getBody(), SessionForm.class);
    GameParametersForm gameParameters = session.gameParameters();

    // Initialize object vars
    this.sessionid = sessionid;

    // TODO: Make this actually set expansion session's expansion
    // Set expansion
    this.expansion = Expansion.ORIENT;
  }

  public String getSessionid() {
    return sessionid;
  }

  public String[] getPlayers() {
    return session.players().toArray(new String[4]);
  }

  public int getNumPlayers() {
    return session.players().size();
  }

  public String getHost() {
    return getPlayers()[0];
  }

  public Expansion getExpansion() {
    return expansion;
  }

  public boolean getLaunched() {
    return session.launched();
  }

  public String getGameServiceLocation() {
    return session.gameParameters().location().contains("server") ? Main.serverLocation :
        session.gameParameters().location();
  }

  public void setSession(SessionForm session) {
    this.session = session;
  }
}
