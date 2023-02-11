package hexanome.fourteen.server.control.form;

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
   *
   * @param location    Location
   * @param name        Name of the Game Service
   * @param displayName Name to be displayed
   */
  public RegisterGameServiceForm(String location, String name, String displayName) {
    this(location, 4, 2, name, displayName, "true");
  }

  /**
   * No args constructor.
   */
  public RegisterGameServiceForm() {
  }

  /**
   * A Getter for the Location.
   *
   * @return Location
   */
  public String location() {
    return location;
  }

  /**
   * A Getter for the Maximum Number of Players in the Session.
   *
   * @return Maximum Number of Players in the Session
   */
  public int maxSessionPlayers() {
    return maxSessionPlayers;
  }

  /**
   * A Getter for the Minimum Number of Players in the Session.
   *
   * @return Minimum Number of Players in the Session
   */
  public int minSessionPlayers() {
    return minSessionPlayers;
  }

  /**
   * A Getter for the Name of the Service.
   *
   * @return Name of the Service
   */
  public String name() {
    return name;
  }

  /**
   * A Getter for the Name to be displayed.
   *
   * @return Name to be displayed
   */
  public String displayName() {
    return displayName;
  }

  /**
   * A Getter for the Web Support.
   *
   * @return Web Support
   */
  public String webSupport() {
    return webSupport;
  }
}
