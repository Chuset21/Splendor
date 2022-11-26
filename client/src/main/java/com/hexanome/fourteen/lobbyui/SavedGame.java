package com.hexanome.fourteen.lobbyui;

public class SavedGame {
  private final String gameName;
  private final String gameID;
  private final String toggleID;
  private final String[] usernames;

  public SavedGame(Object data) {

    String dataString = data.toString();
    int start = dataString.indexOf("[") + 4;
    int end = dataString.indexOf("]") + 1;
    int sep = dataString.indexOf(",");

    this.gameName = dataString.substring(end).replaceAll("'", "");
    this.toggleID = dataString.substring(start, sep);
    this.gameID = "hello";
    this.usernames = new String[] {"John", "Bob"};
  }

  public String getGameName() {
    return this.gameName;
  }

  public String getToggleID() {
    return this.toggleID;
  }
}
