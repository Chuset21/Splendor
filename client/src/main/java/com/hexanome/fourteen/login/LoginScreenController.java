package com.hexanome.fourteen.login;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.StagePayload;
import com.hexanome.fourteen.lobbyui.MenuController;
import com.hexanome.fourteen.lobbyui.ScreenController;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.hexanome.fourteen.lobbyui.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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


  /**
   * Actually displays the LoginScreen for the user to see and interact with.
   *
   * @param stage The Stage that will be used
   * @throws IOException Is thrown when invalid input is found
   */
  @Override
  public void goTo(Stage stage) throws IOException {
    this.stage = stage;

    // Initialize login message
    String message = ((String) ((StagePayload) stage.getUserData()).getPayload());

    if(message != null){
      loginMessageLabel.setText(message);
    } else{
      loginMessageLabel.setText("");
    }
  }

  @FXML
  private void handleLogin() {
    final String username = usernameField.getText().trim();
    final String password = passwordField.getText().trim();
    if (username.isBlank() || password.isBlank()) {
      failedLogin();
      return;
    }

    User user = new User(username);

    if (LobbyServiceCaller.login(username, password, user)) {
      launchGame(user);
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
  private void launchGame(User user) {

    // Try launching the game
    try {
      MenuController.getMenuController(stage).successfulLogin(user);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
