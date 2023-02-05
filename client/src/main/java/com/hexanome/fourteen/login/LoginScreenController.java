package com.hexanome.fourteen.login;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.lobbyui.MenuController;
import com.hexanome.fourteen.lobbyui.ScreenController;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Class that shows the LoginScreen for users.
 */
public final class LoginScreenController implements ScreenController {

  private static Stage aPrimaryStage;

  @FXML
  private TextField usernameField;
  @FXML
  private TextField passwordField;
  @FXML
  private Button loginButton;

  /**
   * Actually displays the LoginScreen for the user to see and interact with.
   *
   * @param stage The Stage that will be used
   * @throws IOException Is thrown when invalid input is found
   */
  @Override
  public void goTo(Stage stage) throws IOException {
    aPrimaryStage = stage;

    // Import root from fxml file
    Parent root =
        FXMLLoader.load(Objects.requireNonNull(LoginScreenController.class.getResource("LoginScreen.fxml")));

    // Set up root on stage (window)
    Scene scene = new Scene(root);

    // Initialize stage settings
    aPrimaryStage.setScene(scene);
    aPrimaryStage.setTitle("Splendor");
    aPrimaryStage.setResizable(false);
    aPrimaryStage.centerOnScreen();

    aPrimaryStage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
  }


  @FXML
  private void handleLogin() {
    final String username = usernameField.getText().trim();
    final String password = passwordField.getText().trim();
    if (username.isBlank() || password.isBlank()) {
      failedLogin();
      return;
    }

    if (LobbyServiceCaller.login(username, password)) {
      LobbyServiceCaller.setUserID(username);
      launchGame(LobbyServiceCaller.getUserID());
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
  private void launchGame(String username) {

    // Try launching the game
    try {
      MenuController.successfulLogin(username, aPrimaryStage);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
