package com.hexanome.fourteen.form.server;

/**
 * Player form.
 */
public final class PlayerForm {
  private String uid;
  private HandForm hand;

  /**
   * Constructor.
   *
   * @param id   the player's user ID
   * @param hand the player's hand
   */
  public PlayerForm(String id, HandForm hand) {
    this.uid = id;
    this.hand = hand;
  }

  /**
   * No args constructor.
   */
  public PlayerForm() {
  }

  /**
   * A Getter for the User ID.
   *
   * @return The User ID
   */
  public String uid() {
    return uid;
  }

  /**
   * A Getter for the Hand.
   *
   * @return The Hand
   */
  public HandForm hand() {
    return hand;
  }
}
