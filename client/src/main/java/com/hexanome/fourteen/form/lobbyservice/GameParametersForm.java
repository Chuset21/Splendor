package com.hexanome.fourteen.form.lobbyservice;

import com.google.gson.annotations.SerializedName;

/**
 * Game parameters class.
 */
public final class GameParametersForm {
  private String location;
  @SerializedName("maxSessionPlayers")
  private int maxSessionPlayers;
  @SerializedName("minSessionPlayers")
  private int minSessionPlayers;
  private String name;

  @SerializedName("webSupport")
  private transient String webSupport;

  /**
   * Constructor.
   */
  public GameParametersForm(String location, int maxSessionPlayers, int minSessionPlayers,
                            String name) {
    this.location = location;
    this.maxSessionPlayers = maxSessionPlayers;
    this.minSessionPlayers = minSessionPlayers;
    this.name = name;
  }

  /**
   * No args constructor.
   */
  public GameParametersForm() {
  }

  public String location() {
    return location;
  }

  public int maxSessionPlayers() {
    return maxSessionPlayers;
  }

  public int minSessionPlayers() {
    return minSessionPlayers;
  }

  public String name() {
    return name;
  }

  public String webSupport() {
    return webSupport;
  }
}
