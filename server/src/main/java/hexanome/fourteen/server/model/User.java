package hexanome.fourteen.server.model;

import com.google.gson.annotations.SerializedName;

/**
 * User.
 */
public final class User {
  private String name;
  @SerializedName("preferredColour")
  private String preferredColour;

  /**
   * No args constructor.
   */
  public User() {
  }

  /**
   * Constructor using a name.
   *
   * @param name The name.
   */
  public User(String name) {
    this.name = name;
  }

  /**
   * A Getter for the Name of a User.
   *
   * @return The Name
   */
  public String name() {
    return name;
  }

  /**
   * A Getter for the Preferred Colour of a User.
   *
   * @return The Preferred Colour
   */
  public String preferredColour() {
    return preferredColour;
  }
}
