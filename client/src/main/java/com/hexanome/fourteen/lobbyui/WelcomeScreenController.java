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

  @Override
  public void goTo(Stage stage) throws IOException {

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
        Objects.requireNonNull(MenuController.class.getResource("choiceSelect.fxml")));
    // Import root from fxml file
    Parent root = loader.load();
    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Welcome");
    stage.setResizable(false);

    stage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    // Displays the current user's username
    displayUsername.setText(LobbyServiceCaller.getUserID());
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
