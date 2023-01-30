package hexanome.fourteen.server.control.form;

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

  /**
   * A Getter for the Creator of a Game.
   *
   * @return The Creator
   */
  public String creator() {
    return creator;
  }

  /**
   * A Getter for the Game Type of a Game.
   *
   * @return The Game Type
   */
  public String gameType() {
    return gameType;
  }

  /**
   * A Getter for the Players of a Game.
   *
   * @return The Players
   */
  public User[] players() {
    return players;
  }

  /**
   * A Getter for the Save Game of a Game.
   *
   * @return The Save Game
   */
  public String saveGame() {
    return saveGame;
  }
}
