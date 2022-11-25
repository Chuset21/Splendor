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

  public String name() {
    return name;
  }

  public String preferredColour() {
    return preferredColour;
  }
}
