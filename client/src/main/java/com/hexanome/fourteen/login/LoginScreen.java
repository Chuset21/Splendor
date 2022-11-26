package com.hexanome.fourteen.login;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.lobbyui.LobbyController;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

/**
 * Class that shows the LoginScreen for users.
 */
public final class LoginScreen implements Initializable {

  private static Stage aPrimaryStage;

  private static final Gson GSON =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();
  public static String accessToken = "";
  public static String refreshToken = "";

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
  public void goToLogin(Stage stage) throws IOException {

    aPrimaryStage = stage;

    // Import root from fxml file
    Parent root =
        FXMLLoader.load(Objects.requireNonNull(LoginScreen.class.getResource("LoginScreen.fxml")));

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

  /**
   * Update the access token.
   */
  public boolean updateAccessToken() {
    HttpResponse<String> response = Unirest.post("%soauth/token".formatted(Main.lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("grant_type", "refresh_token")
        .queryString("refresh_token", refreshToken)
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();
    return getTokens(response);
  }


  /**
   * Login a user.
   *
   * @param username username
   * @param password password
   * @return true if successful, false otherwise.
   */
  public boolean login(String username, String password) {
    HttpResponse<String> response = Unirest.post("%soauth/token".formatted(Main.lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("grant_type", "password")
        .queryString("username", username)
        .queryString("password", password)
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();
    return getTokens(response);
  }

  private boolean getTokens(HttpResponse<String> response) {
    if (response.getStatus() != 200) {
      return false;
    }

    final LoginResponse loginResponse =
        GSON.fromJson(response.getBody(), LoginResponse.class);
    accessToken = loginResponse.accessToken().replaceAll("\\+", "%2B");
    refreshToken = loginResponse.refreshToken().replaceAll("\\+", "%2B");
    return true;
  }

  private void failedLogin() {
    loginButton.setText("Failed login\nTry again");
  }

  @FXML
  private void handleLogin() {
    final String username = usernameField.getText().trim();
    final String password = passwordField.getText().trim();
    if (username.isBlank() || password.isBlank()) {
      failedLogin();
      return;
    }

    if (login(username, password)) {
      launchGame();
    } else {
      failedLogin();
    }
  }

  @FXML
  private void handleQuitButton() {
    Platform.exit();
    System.exit(0);
  }

  private void launchGame() {
    // Create new instance of a Splendor game
    //OrientExpansion game = new OrientExpansion();
    LobbyController game = new LobbyController();

    // Try launching the game
    try {
      game.goToChoiceSelect(aPrimaryStage);
      //game.goToGame(aPrimaryStage);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
