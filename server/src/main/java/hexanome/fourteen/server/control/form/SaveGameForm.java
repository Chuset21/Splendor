package hexanome.fourteen.server.control.form;

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

  /**
   * Getter for the game name.
   *
   * @return The game name.
   */
  public String gameName() {
    return gameName;
  }

  /**
   * Getter the players.
   *
   * @return The players.
   */
  public List<String> players() {
    return players;
  }

  /**
   * Getter for the save game id. Will be empty if the game is not to be loaded from a saved game.
   *
   * @return The save game id.
   */
  public String saveGameid() {
    return saveGameid;
  }
}
