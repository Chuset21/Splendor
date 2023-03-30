package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomeScreenController implements ScreenController {

  @FXML
  private AnchorPane anchorPane;
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
  @FXML
  private Button logoutButton;

  private Stage stage;

  @Override
  public void sendStageData(Stage stage) {
    this.stage = stage;

    // Post init
    // Displays the current user's username
    displayUsername.setText(LobbyServiceCaller.getCurrentUserid());
  }

  @FXML
  public void handleQuitButton() {
    Platform.exit();
    System.exit(0);
  }

  @FXML
  // TODO: Actually change user on next login (LobbyServiceCaller@line97? Always replace instead of only on == null?)
  // or maybe use a http delete request on the user? idk
  public void handleLogoutButton() throws IOException {
    MenuController.returnToLogin("You've successfully logged out.");
  }

  @FXML
  public void handleCreateGameButton() {
    //Switch scene to create game screen (game rule selection)
    try {
      MenuController.goToCreateGameScreen();
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
      MenuController.goToLobbySelectScreen();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @FXML
  public void handleLoadGameButton() {
    //Switch scene to load game details (chose game save)
    try {
      MenuController.goToLoadGameScreen();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
