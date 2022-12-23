package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.form.lobbyservice.GameParametersForm;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;
import com.hexanome.fourteen.form.lobbyservice.SessionsForm;
import java.security.InvalidParameterException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MenuGod {
  private static Stage stage;
  private static Stack<Scene> scenePath = new Stack<>();
  private static String username;


  /**
   * Beginning of Holy Code
   */

  public static void setUsername(String username){
    MenuGod.username = username;
  }

  public static void setStage(Stage stage){
    MenuGod.stage = stage;
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

    MenuGod.username = username;
    MenuGod.stage = stage;

    ScreenController screen = new WelcomeScreenController();
    screen.goTo(stage);
  }

  public static void goToWelcomeScreen() throws IOException {
    ScreenController screen = new WelcomeScreenController();
    screen.goTo(stage);
  }

  public static void goToCreateGameScreen() throws IOException {
    ScreenController screen = new CreateGameScreenController();
    screen.goTo(stage);
  }

  public static void goToLobbySelectScreen() throws IOException {
    ScreenController screen = new LobbySelectScreenController();
    screen.goTo(stage);
  }

  public static void goToLoadGameScreen() throws IOException {
    ScreenController screen = new LoadGameScreenController();
    screen.goTo(stage);
  }

  public static void goToInLobbyScreen() throws IOException {
    ScreenController screen = new InLobbyScreenController();
    screen.goTo(stage);
  }

  /**
   * End of Holy Code
   */

  public void initialize(URL url, ResourceBundle resourceBundle) {
  }
}
