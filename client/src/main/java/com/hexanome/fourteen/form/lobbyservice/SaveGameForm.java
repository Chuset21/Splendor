package com.hexanome.fourteen.form.lobbyservice;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Save game form.
 */
public final class SaveGameForm {
  @SerializedName("gamename")
  private String gameName;
  private List<String> players;

  @SerializedName("savegameid")
  private String saveGameid;

  /**
   * Constructor.
   *
   * @param gameName   the game service name
   * @param players    the players
   * @param saveGameid the save game id
   */
  public SaveGameForm(String gameName, List<String> players, String saveGameid) {
    this.gameName = gameName;
    this.players = players;
    this.saveGameid = saveGameid;
  }

  /**
   * No args constructor.
   */
  public SaveGameForm() {

  }

  public String gameName() {
    return gameName;
  }

  public List<String> players() {
    return players;
  }

  public String saveGameid() {
    return saveGameid;
  }
}
