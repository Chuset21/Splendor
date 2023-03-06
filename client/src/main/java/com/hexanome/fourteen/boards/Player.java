package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.HandForm;
import com.hexanome.fourteen.form.server.PlayerForm;
import com.hexanome.fourteen.form.server.cardform.StandardCardForm;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

/**
 * A Class that implements the functionality needed for a Player.
 */
public class Player extends Image {


  private PlayerForm playerForm;

  /**
   * A Constructor for a Player.
   *
   * @param playerForm form to wrap
   * @param imageFileLocation location of the player's image
   */
  public Player(PlayerForm playerForm, String imageFileLocation) {
    super(imageFileLocation);

    this.playerForm = playerForm;
  }

  public Hand getHand() {
    return new Hand();
  }

  public HandForm getHandForm(){return playerForm.hand();}

  public String getUserId() {
    return playerForm.uid();
  }

  public PlayerForm getPlayerForm() {
    return playerForm;
  }
}
