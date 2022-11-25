package com.hexanome.fourteen.boards;

/**
 * A Class that implements the functionality needed for a Player.
 */
public class Player {
  String userId;
  String username;
  public Hand hand;

  /**
   * A Constructor for a Player.
   *
   * @param userId   The Player's User ID
   * @param username The Player's Username
   */
  public Player(String userId, String username) {
    this.userId = userId;
    this.username = username;
    hand = new Hand();
  }

  public Hand getHand() {
    return hand;
  }

  public String getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

}
