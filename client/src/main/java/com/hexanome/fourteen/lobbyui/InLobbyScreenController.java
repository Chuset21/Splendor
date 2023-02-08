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
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InLobbyScreenController implements ScreenController{

  //TODO: Add "Waiting for player" text in empty player spots

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Text titleText;
  @FXML
  private Text capacityText;
  @FXML
  private GridPane lobbyGrid;
  @FXML
  private Button launchLobbyButton;
  @FXML
  private Button leaveLobbyButton;
  @FXML
  private Button addPlayerButton;

  // Holds data of current lobby (primarily the lobby location)
  private Lobby lobby;
  

  // List of current players
  private DisplayPlayer[] displayPlayers = new DisplayPlayer[4];

  // Temp items to use for making lobbies
  private static final String[] playerImgs = {"cat.jpg","dog.jpg","squirrel.jpg","chameleon.jpg"};
  private static final String[] playerNames = {"Billy Bob", "John Smith", "Gerald", "Betsy", "Brenda"};

  // Template for lobby text
  private static final String LOBBY_NAME_TEMPLATE = "[ownerName]'s Lobby";
  private static final String PLAYER_COUNT_TEMPLATE = "[curPlayers]/[maxPlayers] Players";

  @Override
  public void goTo(Stage stage) throws IOException {

    // Load basic lobby UI
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("InLobbyScreen.fxml")));

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
    this.lobby = (Lobby) MenuController.getStage().getUserData();

    updateLobbyInfo();

    /*try {
      Player player = new Player(playerImgs[new Random().nextInt(4)], MenuOrganizer.getUsername(), this);

      addPlayer(player);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }*/
  }

  @FXML
  private void handleLaunchButton(){

  }

  @FXML
  private void handleLeaveButton(){
    try{
      MenuController.goBack();
    } catch (IOException ioe){
      ioe.printStackTrace();
    }
  }

  @FXML
  private void handleAddPlayerButton(){
    DisplayPlayer displayPlayer = null;

    try {
      displayPlayer = new DisplayPlayer(playerImgs[new Random().nextInt(4)], playerNames[new Random().nextInt(5)], this);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    if (displayPlayer != null) {
      addPlayer(displayPlayer);
    }
  }

  /**
   * Adds a player to the players list and displays them in the lobby
   *
   * @param displayPlayer player to be added
   * @return true if player was added successfully, false if no room was left in lobby
   */
  private boolean addPlayer(DisplayPlayer displayPlayer){
    for(int i = 0;i<4;i++){
      if(displayPlayers[i] == null){
        displayPlayers[i] = displayPlayer;
        lobbyGrid.add(displayPlayer,i/2,i%2);
        return true;
      }
    }
    return false;
  }

  private void updateLobbyInfo(){
    updateLobbyName(lobby.getPlayers()[0]);
    updatePlayerCounter(lobby.getNumPlayers(),lobby.getPlayers().length);
    try{
      addPlayer(new DisplayPlayer(playerImgs[new Random().nextInt(4)],lobby.getPlayers()[0],this));
    } catch(IOException ioe){
      ioe.printStackTrace();
    }
  }

  /**
   * Displays the passed owner name as the lobby owner
   * @param ownerName name to display as owner
   */
  private void updateLobbyName(String ownerName){
    titleText.setText(LOBBY_NAME_TEMPLATE.replace("[ownerName]",ownerName));
  }

  /**
   * Displays the passed player amounts on the lobby screen
   * @param curPlayers players currently in the lobby
   * @param maxPlayers maximum players in the lobby
   */
  private void updatePlayerCounter(int curPlayers,int maxPlayers){
    String temp = PLAYER_COUNT_TEMPLATE.replace("[curPlayers]",""+curPlayers);
    capacityText.setText(temp.replace("[maxPlayers]",""+maxPlayers));
  }

}
