package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.form.lobbyservice.GameParametersForm;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;

public class Lobby {

  private String sessionid;
  private SessionForm session;
  private com.hexanome.fourteen.boards.Expansion expansion;

  public Lobby(String sessionid) {

    // Get settings for session
    session = LobbyServiceCaller.getSessionDetails(sessionid);
    GameParametersForm gameParameters = session.gameParameters();

    // Initialize object vars
    this.sessionid = sessionid;

    // TODO: Make this actually set expansion session's expansion
    // Set expansion
    this.expansion = Expansion.ORIENT;
  }

  public String getSessionid(){
    return sessionid;
  }

  public String[] getPlayers(){
    return session.players().toArray(new String[4]);
  }

  public int getNumPlayers(){
    return session.players().size();
  }

  public String getHost(){
    return getPlayers()[0];
  }

  public Expansion getExpansion(){
    return expansion;
  }

  public void updateLobby(){
    // Get settings for session
    session = LobbyServiceCaller.getSessionDetails(sessionid);
    GameParametersForm gameParameters = session.gameParameters();
  }

}
