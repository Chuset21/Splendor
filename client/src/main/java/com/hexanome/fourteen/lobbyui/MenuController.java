package com.hexanome.fourteen.lobbyui;

import java.security.InvalidParameterException;

import com.hexanome.fourteen.WindowContextData;
import com.hexanome.fourteen.boards.OrientExpansion;
import com.hexanome.fourteen.login.LoginScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import kong.unirest.Empty;

public class MenuController {
  private Stage stage;
  private Stack<ScreenController> previousScreens = new Stack<>();

  /**
   * Factory method to get current menu controller from the stage.
   * Links Stage with MenuController
   *
   * @param stage Stage operating in the current window
   * @return the controller of that stage
   */
  public static MenuController getMenuController(Stage stage){
    if(stage.getUserData() == null || ((WindowContextData)stage.getUserData()).getMenuController() == null){
      stage.setUserData(new WindowContextData(new MenuController(stage)));
    }

    return ((WindowContextData) stage.getUserData()).getMenuController();
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

    ((WindowContextData) stage.getUserData()).setUser(user);

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
    ((WindowContextData) stage.getUserData()).setPayload(errorMessage);
    ((WindowContextData) stage.getUserData()).setUser(null);

    // Load login ui
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("LoginScreen.fxml")));

    // Import root from fxml file
    Parent root = loader.load();

    LoginScreenController controller = loader.getController();
    controller.sendStageData(stage);

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

    ScreenController screen = null;

    try {
      // Get last screen
      screen = previousScreens.pop();
      screen = previousScreens.pop();
    } catch (EmptyStackException e){
      // If no last screen exists, go back to welcome screen
      screen = new WelcomeScreenController();
    }

    // Clear screen stack since you cannot go back after going to welcome screen
    if (screen instanceof WelcomeScreenController) {
      goToWelcomeScreen();
    } else if (screen instanceof CreateGameScreenController) {
      goToCreateGameScreen();
    } else if (screen instanceof LobbySelectScreenController) {
      goToLobbySelectScreen();
    } else if (screen instanceof LoadGameScreenController) {
      goToLoadGameScreen();
    } else if (screen instanceof InLobbyScreenController) {
      goBack();
    } else {
      goToWelcomeScreen();
    }
  }

  public void goToWelcomeScreen() throws IOException {
    // Resets the screen stack since you just got to the welcome menu
    previousScreens.clear();

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
            Objects.requireNonNull(MenuController.class.getResource("WelcomeScreen.fxml")));
    // Import root from fxml file
    Parent root = loader.load();

    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());

    // Go to screen
    ScreenController controller = loader.getController();
    controller.sendStageData(stage);

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Welcome");
    stage.setResizable(false);

    stage.show();

    // Adds current screen to previous screens stack
    previousScreens.push(controller);
  }

  public void goToCreateGameScreen() throws IOException {

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
            Objects.requireNonNull(MenuController.class.getResource("CreateGameScreen.fxml")));
    // Import root from fxml file
    Parent root = loader.load();

    // Go to screen
    ScreenController controller = loader.getController();
    controller.sendStageData(stage);

    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Create Game");
    stage.setResizable(false);

    stage.show();

    // Adds current screen to previous screens stack
    previousScreens.push(controller);
  }

  public void goToLobbySelectScreen() throws IOException {

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
            Objects.requireNonNull(MenuController.class.getResource("LobbySelectScreen.fxml")));
    // Import root from fxml file
    Parent root = loader.load();

    // Go to screen
    ScreenController controller = loader.getController();
    controller.sendStageData(stage);

    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Select Lobby");
    stage.setResizable(false);

    stage.show();

    // Adds current screen to previous screens stack
    previousScreens.push(controller);
  }

  public void goToLoadGameScreen() throws IOException {

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
            Objects.requireNonNull(MenuController.class.getResource("LoadGameScreen.fxml")));
    // Import root from fxml file
    Parent root = loader.load();

    // Go to screen
    ScreenController controller = loader.getController();
    controller.sendStageData(stage);

    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Load Save");
    stage.setResizable(false);

    stage.show();

    // Adds current screen to previous screens stack
    previousScreens.push(controller);
  }

  public void goToInLobbyScreen(Lobby lobby) throws IOException {

    // Load basic lobby UI
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("InLobbyScreen.fxml")));

    // Import root from fxml file
    Parent root = loader.load();

    // Set the current lobby to where the player is
    User.getUser(stage).setCurrentLobby(lobby);

    // Go to screen
    ScreenController controller = loader.getController();
    controller.sendStageData(stage);

    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Lobby");
    stage.setResizable(false);

    stage.show();

    // Adds current screen to previous screens stack
    previousScreens.push(controller);
  }

  public void goToGameBoard() throws IOException {
    // Load basic lobby UI
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
            OrientExpansion.class.getResource("OrientExpansionBoard1600x900.fxml")));

    // Import root from fxml file
    Parent root = loader.load();

    // Go to screen
    OrientExpansion controller = loader.getController();
    controller.goToGame(stage);

    // Set up root on stage (window)
    Scene scene = new Scene(root);

    // Initialize stage settings
    stage.setScene(scene);
    stage.setTitle("Splendor");
    stage.setResizable(false);

    stage.show();
  }
}
