package com.hexanome.fourteen.form.lobbyservice;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Session form.
 */
public final class SessionForm {
  private String creator;
  @SerializedName("gameParameters")
  private GameParametersForm gameParameters;
  private boolean launched;
  private List<String> players;
  @SerializedName("playerLocations")
  private transient Object playerLocations;
  @SerializedName("savegameid")
  private String saveGameid;

  /**
   * Constructor.
   */
  public SessionForm(String creator, GameParametersForm gameParameters, boolean launched,
                     List<String> players, String saveGameid) {
    this.creator = creator;
    this.gameParameters = gameParameters;
    this.launched = launched;
    this.players = players;
    this.saveGameid = saveGameid;
  }

  /**
   * No args constructor.
   */
  public SessionForm() {
  }

  public String creator() {
    return creator;
  }

  public GameParametersForm gameParameters() {
    return gameParameters;
  }

  public boolean launched() {
    return launched;
  }

  public List<String> players() {
    return players;
  }

  public Object playerLocations() {
    return playerLocations;
  }

  public String saveGameid() {
    return saveGameid;
  }

}
