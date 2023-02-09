package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.TokenRefreshFailedException;
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
import javafx.scene.control.Menu;
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

  private Stage aPrimaryStage;

  @Override
  public void goTo(Stage stage) throws IOException {
    aPrimaryStage = stage;

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
    stage.centerOnScreen();

    stage.show();
  }

  // TODO: make this actually send you to login when it fails
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    updateLobbies();

    // TESTING ONLY
    /*GameParametersForm g = new GameParametersForm("place",4,2,"bruh");
    SessionForm s = new SessionForm(username,g,false, new ArrayList<String>(Arrays.asList(username)),"gameid");

    Lobby l = null;
    try{
      // Creates new lobby with the LobbyService's passed values for the following:
      //  - Max players
      //  - Host
      l = new Lobby(lobbyImgs[new Random().nextInt(4)], g.maxSessionPlayers(), com.hexanome.fourteen.boards.Expansion.ORIENT, s.creator(), this);
    } catch(IOException ioe){
      ioe.printStackTrace();
    }

    if (l != null) {
      lobbyVBox.getChildren().add(l);
    }*/
  }

  private void updateLobbies(){
    // Resets displayed lobbies
    lobbyVBox.getChildren().clear();

    SessionsForm lobbyForm = null;

    try{
      lobbyForm = LobbyServiceCaller.getSessions();
    } catch(TokenRefreshFailedException e){
      try{
        MenuController.returnToLogin("Session timed out, retry login");
      } catch(IOException ioe){
        ioe.printStackTrace();
      }
      aPrimaryStage.close();
      return;
    }

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
      MenuController.goBack();
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
    try {
      MenuController.goToInLobbyScreen(lobby);
    } catch (IOException ioe) {
      ioe.printStackTrace();
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
