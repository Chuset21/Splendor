package com.hexanome.fourteen.login;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.lobbyui.MenuController;
import com.hexanome.fourteen.lobbyui.ScreenController;
import java.io.IOException;

import com.hexanome.fourteen.lobbyui.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Class that shows the LoginScreen for users.
 */
public final class LoginScreenController implements ScreenController {

  private Stage stage;

  @FXML
  private TextField usernameField;
  @FXML
  private TextField passwordField;
  @FXML
  private Button loginButton;
  @FXML
  private Label loginMessageLabel;
  @FXML
  private HBox presetCredentialsHBox;
  @FXML
  private Button presetCredentialsButton;


  /**
   * Actually displays the LoginScreen for the user to see and interact with.
   *
   * @param stage The Stage that will be used
   * @throws IOException Is thrown when invalid input is found
   */
  @Override
  public void sendStageData(Stage stage) {
    this.stage = stage;

    // Initialize login message
    String message = (String) stage.getUserData();

    if(message != null){
      loginMessageLabel.setText(message);
    } else{
      loginMessageLabel.setText("");
    }
    presetCredentialsButton.toFront();
    presetCredentialsHBox.toFront();
  }



  @FXML
  private void handleLogin() {
    final String username = usernameField.getText().trim();
    final String password = passwordField.getText().trim();
    if (username.isBlank() || password.isBlank()) {
      failedLogin();
      return;
    }

    User user = new User(username, password);

    if (LobbyServiceCaller.login(username, password)) {
      launchGame();
    } else {
      failedLogin();
    }
  }

  private void failedLogin() {
    loginButton.setText("Failed login, try again");
  }

  @FXML
  private void handleQuitButton() {
    Platform.exit();
    System.exit(0);
  }

  /**
   * Creates new lobby instance and routes the player through it
   */
  private void launchGame() {

    // Try launching the game
    try {
      MenuController.goToWelcomeScreen();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  // DEV MODE
  @FXML
  private void togglePresetLogins() {
    presetCredentialsHBox.setVisible(!presetCredentialsHBox.isVisible());
  }
  @FXML
  private void loginLinus() { loginPreset("linus");}
  @FXML
  private void loginMaex() { loginPreset("maex");}
  @FXML
  private void loginKhabiir() { loginPreset("khabiir");}
  @FXML
  private void loginMarianick() { loginPreset("marianick");}

  private void loginPreset(String username) {


    User user = new User(username, "abc123_ABC123");

    if (LobbyServiceCaller.login(username, "abc123_ABC123")) {
      launchGame();
    } else {
      failedLogin();
    }
  }


}
