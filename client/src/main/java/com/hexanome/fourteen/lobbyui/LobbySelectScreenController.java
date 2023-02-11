package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;
import com.hexanome.fourteen.form.lobbyservice.SessionsForm;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LobbySelectScreenController implements ScreenController{

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private ScrollPane lobbyScrollView;
  @FXML
  private VBox lobbyVBox;
  @FXML
  private Button backButton;
  @FXML
  private Button addLobby;

  private Stage stage;

  @Override
  public void goTo(Stage stage) throws IOException {
    this.stage = stage;

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
        Objects.requireNonNull(MenuController.class.getResource("joinGame.fxml")));
    // Import root from fxml file
    Parent root = loader.load();
    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Select Lobby");
    stage.setResizable(false);

    stage.show();

    // Post init
    updateLobbies();
  }

  private void updateLobbies(){
    // Resets displayed lobbies
    lobbyVBox.getChildren().clear();

    SessionsForm lobbyForm = lobbyForm = LobbyServiceCaller.getSessions();

    if(lobbyForm != null){
      Map<String, SessionForm> lobbies = lobbyForm.sessions();

      // Iterates through all active sessions
      for(Map.Entry<String, SessionForm> entry : lobbies.entrySet()){
        // Creates new lobby based on session data from lobby service
        Lobby lobby = new Lobby(entry.getKey());
        DisplayLobby displayLobby = null;

        try{
          // Creates new display lobby based on data received from lobby service

          displayLobby = new DisplayLobby(lobby, this);
        } catch(IOException ioe){
          ioe.printStackTrace();
        }

        if (displayLobby != null) {
          lobbyVBox.getChildren().add(displayLobby);
        }
      }
    }
  }

  @FXML
  private void handleBackButton(){
    try {
      MenuController.getMenuController(stage).goBack();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * Join given lobby
   * This function is only used when called by the Lobby object,
   * never directly from FXML.
   */
  public void handleJoinLobbyButton(Lobby lobby){
    if(!lobby.getHost().equals(User.getUserid(stage))){
      try{
        if(LobbyServiceCaller.joinSession(lobby.getSessionid(),User.getUser(stage))) { MenuController.getMenuController(stage).goToInLobbyScreen(lobby); }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else{
      try{
        MenuController.getMenuController(stage).goToInLobbyScreen(lobby);
      } catch (IOException ioe){
        ioe.printStackTrace();
      }
    }
  }

  @FXML
  private void handleAddLobby() {
//    Lobby lobby = null;
//
//    try {
//      lobby = new Lobby(lobbyImgs[new Random().nextInt(4)],"location", 3, com.hexanome.fourteen.boards.Expansion.ORIENT, lobbyHosts[new Random().nextInt(5)], this);
//    } catch (IOException ioe) {
//      ioe.printStackTrace();
//    }
//
//    if (lobby != null) {
//      lobbyVBox.getChildren().add(lobby);
//    }
  }
}
