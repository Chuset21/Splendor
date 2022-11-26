package hexanome.fourteen.server.control;

import com.google.gson.annotations.SerializedName;

/**
 * Register game service form.
 */
public final class RegisterGameServiceForm {
  private String location;
  @SerializedName("maxSessionPlayers")
  private int maxSessionPlayers;
  @SerializedName("minSessionPlayers")
  private int minSessionPlayers;
  private String name;
  @SerializedName("displayName")
  private String displayName;
  @SerializedName("webSupport")
  private String webSupport;

  /**
   * Constructor.
   *
   * @param location          location
   * @param maxSessionPlayers maximum number of players
   * @param minSessionPlayers minimum number of players
   * @param name              name of the game service
   * @param displayName       name to be displayed
   * @param webSupport        web support
   */
  public RegisterGameServiceForm(String location, int maxSessionPlayers, int minSessionPlayers,
                                 String name, String displayName, String webSupport) {
    this.location = location;
    this.maxSessionPlayers = maxSessionPlayers;
    this.minSessionPlayers = minSessionPlayers;
    this.name = name;
    this.displayName = displayName;
    this.webSupport = webSupport;
  }

  /**
   * Constructor.
   */
  public RegisterGameServiceForm(String location, String name, String displayName) {
    this(location, 4, 2, name, displayName, "true");
  }

  /**
   * No args constructor.
   */
  public RegisterGameServiceForm() {

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

  public String displayName() {
    return displayName;
  }

  public String webSupport() {
    return webSupport;
  }
}
