package com.hexanome.fourteen.lobbyui;

import java.security.InvalidParameterException;

import com.hexanome.fourteen.StagePayload;
import com.hexanome.fourteen.login.LoginScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MenuController {
  private Stage stage;
  private Stack<ScreenController> previousScreens = new Stack<>();
  private ScreenController currentScreen = null;

  /**
   * Factory method to get current menu controller from the stage.
   * Links Stage with MenuController
   *
   * @param stage Stage operating in the current window
   * @return the controller of that stage
   */
  public static MenuController getMenuController(Stage stage){
    if(stage.getUserData() == null || ((StagePayload)stage.getUserData()).getMenuController() == null){
      stage.setUserData(new StagePayload(new MenuController(stage)));
    }

    return ((StagePayload) stage.getUserData()).getMenuController();
  }

  private MenuController(Stage stage){
    this.stage = stage;
  }

  public Stage getStage() {
    return stage;
  }

  public void successfulLogin(User user) throws IOException {
    if (user == null) {
      throw new InvalidParameterException("User parameter cannot be null.");
    }

    ((StagePayload) stage.getUserData()).setUser(user);

    goToWelcomeScreen();
  }

  /**
   * Go to login screen to input credentials again
   *
   * @param errorMessage message to be displayed to the user (e.g. "Session timed out, retry login")
   * @throws IOException
   */
  public void returnToLogin(String errorMessage) throws IOException{
    // Send errorMessage
    ((StagePayload) stage.getUserData()).setPayload(errorMessage);
    ((StagePayload) stage.getUserData()).setUser(null);

    // Load login ui
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("LoginScreen.fxml")));

    // Import root from fxml file
    Parent root = loader.load();

    LoginScreenController controller = loader.getController();
    controller.goTo(stage);

    // Set up root on stage (window)
    Scene scene = new Scene(root);

    // Initialize stage settings
    this.stage.setScene(scene);
    this.stage.setTitle("Splendor");
    this.stage.setResizable(false);

    this.stage.show();
  }

  // TODO: Make this change where you end up depending on which screen you are at
  public void goBack() throws IOException {
    // Get last screen
    ScreenController screen = previousScreens.pop();

    currentScreen = screen;

    // Clear screen stack since you cannot go back after going to welcome screen
    if (screen instanceof WelcomeScreenController) {
      previousScreens.clear();
    }

    // Go to last screen
    screen.goTo(stage);
  }

  public void goToWelcomeScreen() throws IOException {
    // Resets the screen stack since you just got to the welcome menu
    previousScreens.clear();

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
            Objects.requireNonNull(MenuController.class.getResource("choiceSelect.fxml")));
    // Import root from fxml file
    Parent root = loader.load();

    // Go to screen
    ScreenController controller = loader.getController();
    controller.goTo(stage);

    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Welcome");
    stage.setResizable(false);

    stage.show();

    // Sets this screen as current screen
    currentScreen = controller;
  }

  public void goToCreateGameScreen() throws IOException {
    // Adds current screen to previous screens stack
    previousScreens.push(currentScreen);
    ScreenController screen = new CreateGameScreenController();

    // Sets this screen as current screen
    currentScreen = screen;
    screen.goTo(stage);
  }

  public void goToLobbySelectScreen() throws IOException {
    // Adds current screen to previous screens stack
    previousScreens.push(currentScreen);
    ScreenController screen = new LobbySelectScreenController();

    // Sets this screen as current screen
    currentScreen = screen;
    screen.goTo(stage);
  }

  public void goToLoadGameScreen() throws IOException {
    // Adds current screen to previous screens stack
    previousScreens.push(currentScreen);
    ScreenController screen = new LoadGameScreenController();

    // Sets this screen as current screen
    currentScreen = screen;
    screen.goTo(stage);
  }

  public void goToInLobbyScreen(Lobby lobby) throws IOException {
    // Adds current screen to previous screens stack
    previousScreens.push(currentScreen);
    ScreenController screen = new InLobbyScreenController();

    // Set the current lobby the player is in
    User.getUser(stage).setCurrentLobby(lobby);

    // Sets this screen as current screen
    currentScreen = screen;
    screen.goTo(stage);
  }

  public void initialize(URL url, ResourceBundle resourceBundle) {
  }
}
