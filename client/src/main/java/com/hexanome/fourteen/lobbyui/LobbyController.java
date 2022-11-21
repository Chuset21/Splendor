package com.hexanome.fourteen.lobbyui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LobbyController implements Initializable {
  private static Stage aPrimaryStage;
  private static Stack<Scene> scenePath = new Stack();

  private String username;

  @FXML
  private Button newGameButton;
  @FXML
  private Button joinGameButton;
  @FXML
  private Button createGameButton;
  @FXML
  private Button quitButton;
  @FXML
  private AnchorPane choiceSelectAnchor;
  @FXML
  private Pane opacityPane;
  @FXML
  private Pane titleOpacityPane;
  @FXML
  private Pane buttonsOpacityPane;
  @FXML
  private Button backButton;

  public void goToChoiceSelect(Stage pStage) throws IOException {
    aPrimaryStage = pStage;

    // Import root from fxml file
    Parent root = FXMLLoader.load(Objects.requireNonNull(
        LobbyController.class.getResource("choiceSelect.fxml")));
    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());


    // Initialize stage settings
    aPrimaryStage.setScene(aScene);
    aPrimaryStage.setTitle("Splendor - Menu");
    aPrimaryStage.setResizable(false);
    aPrimaryStage.centerOnScreen();

    aPrimaryStage.show();
  }

  private void init() {
    username = "joebiden43";
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    init();
  }

  public void loadScene(Parent root) {
    //Push the current scene to the scene stack
    scenePath.push(aPrimaryStage.getScene());

    //Load Scene
    Scene scene = new Scene(root);
    aPrimaryStage.setScene(scene);
    aPrimaryStage.show();
  }

  public void unloadScene() {
    //Load top scene in ScenePath stack
    aPrimaryStage.setScene(scenePath.pop());
    aPrimaryStage.show();
  }

  public void handleCreateGameButton() {
    //Switch scene to create game details (game rule selection)
    try {
      Parent root = FXMLLoader.load(getClass().getResource("createGame.fxml"));
      loadScene(root);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void handleJoinGameButton() {
    //Switch scene to join game details (lobby selection)
    try {
      Parent root = FXMLLoader.load(getClass().getResource("joinGame.fxml"));
      loadScene(root);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void handleLoadGameButton() {
    //Switch scene to load game details (chose game save)
    try {
      Parent root = FXMLLoader.load(getClass().getResource("loadGame.fxml"));
      loadScene(root);
    } catch(Exception e) {
      e.printStackTrace();
    }

  }
  public void handleQuitButton() {
    Platform.exit();
    System.exit(0);
  }
}
