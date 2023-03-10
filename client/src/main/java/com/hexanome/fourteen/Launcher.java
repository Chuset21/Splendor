package com.hexanome.fourteen;

import com.hexanome.fourteen.lobbyui.MenuController;
import javafx.application.Application;
import javafx.stage.Stage;


public class Launcher extends Application {

  public static void main(String[] args) {
    Application.launch(Launcher.class, args);
  }

  /**
   * Run on startup of application. Sends user to login screen
   *
   * @param stage active stage passed by application
   */
  @Override
  public void start(Stage stage) throws Exception {

    // Save this window's stage for future use
    MenuController.setStage(stage);

    // Start login
    MenuController.returnToLogin("");
  }
}