package com.hexanome.fourteen.lobbyui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class InLobbyScreenController implements ScreenController{

  @FXML
  private GridPane lobbyGrid;

  // List of current players
  private Player[] players = new Player[4];

  // Temp items to use for making lobbies
  private static final String[] playerImgs = {"cat.jpg","dog.jpg","squirrel.jpg","chameleon.jpg"};
  private static final String[] playerNames = {"Billy Bob", "John Smith", "Gerald", "Betsy", "Brenda"};

  @Override
  public void goTo(Stage stage) throws IOException {

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
        Objects.requireNonNull(MenuOrganizer.class.getResource("lobby.fxml")));
    // Import root from fxml file
    Parent root = loader.load();
    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Lobby");
    stage.setResizable(false);
    stage.centerOnScreen();

    stage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      Player player = new Player(playerImgs[new Random().nextInt(4)], MenuOrganizer.getUsername(), this);

      addPlayer(player);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @FXML
  private void handleLaunchButton(){

  }

  @FXML
  private void handleLeaveButton(){
    try{
      MenuOrganizer.goBack();
    } catch (IOException ioe){
      ioe.printStackTrace();
    }
  }

  @FXML
  private void handleAddPlayerButton(){
    Player player = null;

    try {
      player = new Player(playerImgs[new Random().nextInt(4)], playerNames[new Random().nextInt(5)], this);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    if (player != null) {
      addPlayer(player);
    }
  }

  /**
   * Adds a player to the players list and displays them in the lobby
   *
   * @param player player to be added
   * @return true if player was added successfully, false if no room was left in lobby
   */
  private boolean addPlayer(Player player){
    for(int i = 0;i<4;i++){
      if(players[i] == null){
        players[i] = player;
        lobbyGrid.add(player,i/2,i%2);
        return true;
      }
    }
    return false;
  }

}
