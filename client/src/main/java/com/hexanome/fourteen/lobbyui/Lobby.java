package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.form.lobbyservice.GameParametersForm;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;

public class Lobby {

  private String sessionid;
  private int numPlayers;
  private String[] players;
  private com.hexanome.fourteen.boards.Expansion expansion;

  public Lobby(String sessionid) {

    // Get settings for session
    SessionForm session = LobbyServiceCaller.getSessionDetails(sessionid);
    GameParametersForm gameParameters = session.gameParameters();

    // Initialize object vars
    this.sessionid = sessionid;

    // Set player list
    this.numPlayers = 1;
    this.players = new String[gameParameters.maxSessionPlayers()];
    players[0] = LobbyServiceCaller.getUserID();

    // TODO: Make this actually set expansion session's expansion
    // Set expansion
    this.expansion = Expansion.ORIENT;
  }

  public String[] getPlayers(){
    return players;
  }

  public int getNumPlayers(){
    return numPlayers;
  }

  public Expansion getExpansion(){
    return expansion;
  }

}
