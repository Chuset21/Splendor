package com.hexanome.fourteen.lobbyui;

import java.security.InvalidParameterException;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MenuOrganizer {
  private static Stage stage;
  private static Stack<ScreenController> previousScreens = new Stack<>();
  private static ScreenController currentScreen = null;
  private static String username;

  public static void setUsername(String username){
    MenuOrganizer.username = username;
  }

  public static void setStage(Stage stage){
    MenuOrganizer.stage = stage;
  }

  public static String getUsername(){
    return ""+username;
  }

  public static Stage getStage(){
    return stage;
  }

  public static void successfulLogin(String username, Stage stage) throws IOException{
    if(username == null || stage == null){
      throw new InvalidParameterException("Neither parameter can be null.");
    }

    // Initializes user settings into menu system
    MenuOrganizer.username = username;
    MenuOrganizer.stage = stage;

    ScreenController screen = new WelcomeScreenController();

    // Resets the screen stack since you just got to the welcome menu
    previousScreens.clear();

    // Sets this screen as current screen
    currentScreen = screen;
    screen.goTo(stage);
  }

  public static void goBack() throws IOException{
    // Get last screen
    ScreenController screen = previousScreens.pop();

    currentScreen = screen;

    // Clear screen stack since you cannot go back after going to welcome screen
    if(screen instanceof WelcomeScreenController){
      previousScreens.clear();
    }

    // Go to last screen
    screen.goTo(stage);
  }

  public static void goToWelcomeScreen() throws IOException {
    ScreenController screen = new WelcomeScreenController();

    // Clear screen stack since you cannot go back after going to welcome screen
    previousScreens.clear();

    // Sets this screen as current screen
    currentScreen = screen;
    screen.goTo(stage);
  }

  public static void goToCreateGameScreen() throws IOException {
    // Adds current screen to previous screens stack
    previousScreens.push(currentScreen);
    ScreenController screen = new CreateGameScreenController();

    // Sets this screen as current screen
    currentScreen = screen;
    screen.goTo(stage);
  }

  public static void goToLobbySelectScreen() throws IOException {
    // Adds current screen to previous screens stack
    previousScreens.push(currentScreen);
    ScreenController screen = new LobbySelectScreenController();

    // Sets this screen as current screen
    currentScreen = screen;
    screen.goTo(stage);
  }

  public static void goToLoadGameScreen() throws IOException {
    // Adds current screen to previous screens stack
    previousScreens.push(currentScreen);
    ScreenController screen = new LoadGameScreenController();

    // Sets this screen as current screen
    currentScreen = screen;
    screen.goTo(stage);
  }

  public static void goToInLobbyScreen() throws IOException {
    // Adds current screen to previous screens stack
    previousScreens.push(currentScreen);
    ScreenController screen = new InLobbyScreenController();

    // Sets this screen as current screen
    currentScreen = screen;
    screen.goTo(stage);
  }

  public void initialize(URL url, ResourceBundle resourceBundle) {
  }
}
