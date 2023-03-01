package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;

import com.hexanome.fourteen.boards.GameBoard;
import com.hexanome.fourteen.login.LoginScreenController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

public class MenuController {
  private static Stage stage;
  private static Stack<ScreenController> previousScreens = new Stack<>();

  /**
   * Gets window's stage
   * @return stage
   */
  public static Stage getStage() {
    return stage;
  }

  /**
   * Sets window's stage
   * @param stage stage
   */
  public static void setStage(Stage stage) {
    MenuController.stage = stage;
  }

  /**
   * Go to login screen to input credentials again
   *
   * @param errorMessage message to be displayed to the user (e.g. "Session timed out, retry login")
   * @throws IOException
   */
  public static void returnToLogin(String errorMessage) throws IOException{
    // Send errorMessage
    stage.setUserData(errorMessage);

    // Load login ui
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("LoginScreen.fxml")));

    // Import root from fxml file
    Parent root = loader.load();

    LoginScreenController controller = loader.getController();
    controller.sendStageData(stage);

    // Set up root on stage (window)
    Scene scene = new Scene(root);

    // Initialize stage settings
    stage.setScene(scene);
    stage.setTitle("Splendor");
    stage.setResizable(false);

    stage.show();
  }

  // TODO: Make this change where you end up depending on which screen you are at
  public static void goBack() throws IOException {

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

  public static  void goToWelcomeScreen() throws IOException {
    // Resets the screen stack since you just got to the welcome menu
    previousScreens.clear();

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
            Objects.requireNonNull(MenuController.class.getResource("WelcomeScreen.fxml")));
    // Import root from fxml file
    Parent root = loader.load();

    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(MenuController.class.getResource("lobbyStyling.css").toExternalForm());

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

  public static void goToCreateGameScreen() throws IOException {

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
    aScene.getStylesheets().add(MenuController.class.getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Create Game");
    stage.setResizable(false);

    stage.show();

    // Adds current screen to previous screens stack
    previousScreens.push(controller);
  }

  public static void goToLobbySelectScreen() throws IOException {

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
    aScene.getStylesheets().add(MenuController.class.getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Select Lobby");
    stage.setResizable(false);

    stage.show();

    // Adds current screen to previous screens stack
    previousScreens.push(controller);
  }

  public static void goToLoadGameScreen() throws IOException {

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
    aScene.getStylesheets().add(MenuController.class.getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Load Save");
    stage.setResizable(false);

    stage.show();

    // Adds current screen to previous screens stack
    previousScreens.push(controller);
  }

  public static void goToInLobbyScreen(Lobby lobby) throws IOException {
    // Set the current lobby to where the player is
    LobbyServiceCaller.setCurrentUserLobby(lobby);

    // Load basic lobby UI
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("InLobbyScreen.fxml")));

    // Import root from fxml file
    Parent root = loader.load();

    // Go to screen
    ScreenController controller = loader.getController();
    controller.sendStageData(stage);

    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(MenuController.class.getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Lobby");
    stage.setResizable(false);

    stage.show();

    // Adds current screen to previous screens stack
    previousScreens.push(controller);
  }

  public static void goToGameBoard() throws IOException {
    // Load basic lobby UI
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
            GameBoard.class.getResource("OrientExpansionBoard1600x900.fxml")));

    // Import root from fxml file
    Parent root = loader.load();

    // Go to screen
    GameBoard controller = loader.getController();
    controller.goToGame(stage);

    // Set up root on stage (window)
    Scene scene = new Scene(root);

    // Initialize stage settings
    stage.setScene(scene);
    stage.setTitle("Splendor");

    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    double x = (screenBounds.getWidth() - stage.getWidth()) / 2;
    double y = (screenBounds.getHeight() - stage.getHeight()) / 2;
    stage.setX(x);
    stage.setY(y);

    stage.setResizable(false);
    stage.setMaximized(true);

    stage.show();
  }
}
