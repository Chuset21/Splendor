package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.GameServiceName;
import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;
import kong.unirest.HttpResponse;

public class Lobby {

  private final String sessionid;
  private SessionForm session;
  private final GameServiceName gameServiceName;

  public Lobby(String sessionid) {

    // Get settings for session
    final HttpResponse<String> response = LobbyServiceCaller.getSessionDetails(sessionid);
    session = Main.GSON.fromJson(response.getBody(), SessionForm.class);

    // Initialize object vars
    this.sessionid = sessionid;

    this.gameServiceName = GameServiceName.valueOf(session.gameParameters().name());
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

  public GameServiceName getGameServiceName() {
    return gameServiceName;
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
