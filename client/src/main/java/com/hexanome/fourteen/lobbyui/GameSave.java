package com.hexanome.fourteen.lobbyui;

public class GameSave {
  private final String name;
  private final String toggleID;

  public GameSave(String data) {
    int start = data.indexOf("[") + 4;
    int end = data.indexOf("]") + 1;
    int sep = data.indexOf(",");

    this.name = data.substring(end).replaceAll("'", "");
    this.toggleID = data.substring(start, sep);
  }

  public String getName() {
    return this.name;
  }

  public String getToggleID() {
    return this.toggleID;
  }
}
