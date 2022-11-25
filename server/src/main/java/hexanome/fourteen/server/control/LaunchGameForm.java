package hexanome.fourteen.server.control;

import com.google.gson.annotations.SerializedName;
import hexanome.fourteen.server.model.User;

/**
 * Launch game form.
 */
public final class LaunchGameForm {
  private String creator;
  @SerializedName("gameServer")
  private String gameType; // This will tell us the expansion
  private User[] players;
  @SerializedName("savegame")
  private String saveGame;

  /**
   * No args constructor.
   */
  public LaunchGameForm() {
  }

  public String creator() {
    return creator;
  }

  public String gameServer() {
    return gameType;
  }

  public User[] players() {
    return players;
  }

  public String saveGame() {
    return saveGame;
  }
}
