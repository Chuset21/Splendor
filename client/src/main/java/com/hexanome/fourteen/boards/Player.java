package com.hexanome.fourteen.boards;

public class Player {
  String aUserID;
  String aUsername;
  public Hand aHand;

  public Player(String pUserID, String pUsername) {
    aUserID = pUserID;
    aUsername = pUsername;
    aHand = new Hand();
  }

  public Hand getHand() {
    return aHand;
  }

  public String getUserID() {
    return aUserID;
  }

  public String getUsername() {
    return aUsername;
  }

}
