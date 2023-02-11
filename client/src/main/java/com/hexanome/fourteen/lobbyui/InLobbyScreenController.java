package com.hexanome.fourteen.lobbyui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.StagePayload;
import com.hexanome.fourteen.TokenRefreshFailedException;
import com.hexanome.fourteen.boards.OrientExpansion;
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

  // Temp names to use for players
  private static final String[] playerNames = {"Billy Bob", "John Smith", "Gerald", "Betsy", "Brenda"};

  // Template for lobby text
  private static final String LOBBY_NAME_TEMPLATE = "[ownerName]'s Lobby";
  private static final String PLAYER_COUNT_TEMPLATE = "[curPlayers]/[maxPlayers] Players";

  private Stage stage;

  @Override
  public void goTo(Stage stage) throws IOException {
    this.stage = stage;

    // Get data from MenuController
    this.lobby = User.getCurrentLobby(stage);

    updateLobbyInfo();
  }

  @FXML
  private void handleLaunchButton(){
    if(User.getUserid(stage).equals(lobby.getHost())){
      OrientExpansion oe = new OrientExpansion();

      try{
        if(LobbyServiceCaller.launchSession(User.getUser(stage))) {oe.goToGame(stage);}
      } catch(Exception e){
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void handleLeaveButton(){
    if(User.getUserid(stage).equals(lobby.getHost())) {
      try{
        LobbyServiceCaller.deleteSession(User.getUser(stage));
      } catch(TokenRefreshFailedException e){
        try{
          MenuController.getMenuController(stage).returnToLogin("Session timed out, retry login");
          stage.close();
          return;
        } catch(IOException ioe){
          ioe.printStackTrace();
        }
      }
    }

    try{
      MenuController.getMenuController(stage).goBack();

      User.getUser(stage).setCurrentLobby(null);
    } catch (IOException ioe){
      ioe.printStackTrace();
    }
  }

  @FXML
  private void handleAddPlayerButton(){
    DisplayPlayer displayPlayer = null;

    try {
      displayPlayer = new DisplayPlayer(new Player(playerNames[new Random().nextInt(5)], "Green"), this);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    if (displayPlayer != null) {
      addPlayer(displayPlayer);
    }
  }

  /**
   * LEGACY
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
    updateLobbyPlayers(lobby);
  }

  private void updateLobbyPlayers(Lobby aLobby){
    lobbyGrid.getChildren().clear();

    for(int i = 0;i<aLobby.getNumPlayers();i++){
      DisplayPlayer player = null;

      try{
        player = new DisplayPlayer(new Player(aLobby.getPlayers()[i], "Green"), this);
      } catch(IOException ioe){
        ioe.printStackTrace();
      }

      if(player != null){
        lobbyGrid.add(player,i/2,i%2);
      }
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
