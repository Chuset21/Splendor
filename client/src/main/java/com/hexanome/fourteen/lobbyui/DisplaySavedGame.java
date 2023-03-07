package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.form.lobbyservice.SaveGameForm;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DisplaySavedGame extends Pane implements Initializable {


  @FXML
  private ToggleButton savedGameToggle;

  @FXML
  private ImageView image;

  @FXML
  private Text playersText;

  @FXML
  private Text expansionText;

  @FXML
  private Text gameidText;

  private LoadGameScreenController controller;

  public DisplaySavedGame(SaveGameForm savedGame, LoadGameScreenController controller)
      throws IOException {
    FXMLLoader loader =
        new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("displaySavedGame.fxml")));

    // Apply the UI to this class
    loader.setRoot(this);

    // THIS IS IMPORTANT, you can't set this object as the root unless the FXML file has NO CONTROLLER SET & it has fx:root="Pane"
    loader.setController(this);
    loader.load();

    // Set the User Data as GameID
    savedGameToggle.setUserData(savedGame.saveGameid());

    // Set their Text
    expansionText.setText(savedGame.gameName());
    playersText.setText(String.join(",", savedGame.players()));
    gameidText.setText(savedGame.saveGameid());

    this.controller = controller;
  }

  public void setToggleGroup(ToggleGroup toggleGroup) {
    savedGameToggle.setToggleGroup(toggleGroup);
  }


  // Temp items to use for making lobbies
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
  }
}
