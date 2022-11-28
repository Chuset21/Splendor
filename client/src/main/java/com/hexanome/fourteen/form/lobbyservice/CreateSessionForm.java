package com.hexanome.fourteen.form.lobbyservice;

import com.google.gson.annotations.SerializedName;

/**
 * Create session form.
 */
public final class CreateSessionForm {
  private String creator;
  private String game;
  @SerializedName("savegame")
  private String saveGame;

  /**
   * Constructor.
   *
   * @param creator  creator
   * @param game     game to be played
   * @param saveGame save game id
   */
  public CreateSessionForm(String creator, String game, String saveGame) {
    this.creator = creator;
    this.game = game;
    this.saveGame = saveGame;
  }

  /**
   * Constructor for creation a session not from a save game.
   *
   * @param creator creator
   * @param game    game to be played
   */
  public CreateSessionForm(String creator, String game) {
    this(creator, game, "");
  }

  /**
   * No args constructor.
   */
  public CreateSessionForm() {
  }

  public String creator() {
    return creator;
  }

  public String game() {
    return game;
  }

  public String saveGame() {
    return saveGame;
  }
}
