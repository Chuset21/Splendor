package com.hexanome.fourteen.lobbyui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.hexanome.fourteen.LobbyServiceCaller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomeScreenController implements ScreenController{

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Text welcomeText;
  @FXML
  private Text displayUsername;
  @FXML
  private Button newGameButton;
  @FXML
  private Button joinGameButton;
  @FXML
  private Button createGameButton;
  @FXML
  private Button quitButton;

  private Stage stage;

  @Override
  public void sendStageData(Stage stage) throws IOException {
    this.stage = stage;

    // Post init
    // Displays the current user's username
    displayUsername.setText(User.getUserid(stage));
  }

  @FXML
  public void handleQuitButton() {
    Platform.exit();
    System.exit(0);
  }

  @FXML
  public void handleCreateGameButton() {
    //Switch scene to create game screen (game rule selection)
    try {
      MenuController.getMenuController(stage).goToCreateGameScreen();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * Goes to the lobby selection screen
   */
  @FXML
  public void handleJoinGameButton() {
    //Switch scene to join game details (lobby selection)
    try {
      MenuController.getMenuController(stage).goToLobbySelectScreen();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @FXML
  public void handleLoadGameButton() {
    //Switch scene to load game details (chose game save)
    try {
      MenuController.getMenuController(stage).goToLoadGameScreen();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
