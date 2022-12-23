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

  // Temp items to use for making lobbies
  private static final String[] lobbyImgs = {"cat.jpg","dog.jpg","squirrel.jpg","chameleon.jpg"};
  private static final String[] lobbyHosts = {"Billy Bob", "John Smith", "Gerald", "Betsy", "Brenda"};

  @Override
  public void goTo(Stage stage) throws IOException {

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
        Objects.requireNonNull(MenuOrganizer.class.getResource("joinGame.fxml")));
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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    /*SessionsForm lobbyform = LobbyServiceCaller.getSessions();
    Map<String, SessionForm> lobbies = lobbyform.sessions();

    for(Map.Entry<String, SessionForm> entry : lobbies.entrySet()){
      Lobby l = null;
      SessionForm s = entry.getValue();
      GameParametersForm g = s.gameParameters();

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
      }
    }*/

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

  @FXML
  private void handleBackButton(){
    try {
      MenuOrganizer.goBack();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * Join given lobby
   * This function is only used when called by the Lobby object,
   * never directly from FXML.
   */
  public void handleJoinLobbyButton(){
    try {
      MenuOrganizer.goToInLobbyScreen();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * Fernando's code to init the lobby
   */
  /*public void handleJoinLobbyButton() {
//    try {
//      Parent root = FXMLLoader.load(getClass().getResource("lobby.fxml"));
//      loadScene(root);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }

    try {
      //Push the current scene to the scene stack
      scenePath.push(stage.getScene());

      //Initialize the lobby selector and it's grid
      initInnerLobby();

      for (int i = 0; i < 4; i++) {
        Text curr = (Text) playerGrid.getChildren().get(i);
        Text name = new Text(username);
        name.setFont(Font.font("Satoshi", 25));

        if (curr.getText() == "Waiting for player...") {
          playerGrid.getChildren().set(i, name);
          break;
        }
      }

      //Create, style, and display scene
      Scene scene = new Scene(container);
      stage.setScene(scene);
      stage.show();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }*/

  /*
  private void initInnerLobby() {

    playerOne = new Text("Waiting for player...");
    playerOne.setFont(Font.font("Satoshi", 20));


    playerTwo = new Text("Waiting for player...");
    playerTwo.setFont(Font.font("Satoshi", 20));

//    Text notReadyText = new Text("NOT Ready");
//    notReadyText.setFont(Font.font ("Satoshi", 20));
//    notReadyText.setFill(Color.RED);


    playerGrid.add(playerOne, 0, 0);
    playerGrid.add(playerTwo, 1, 0);

    innerLobby.getChildren().add(playerGrid);
    playerGrid.setPrefSize(1100, 600);
    playerGrid.setPadding(new Insets(80, 20, 20, 20));
    playerGrid.setHgap(50);
    playerGrid.setVgap(20);

    readyButton.setFont(Font.font("Satoshi", 20));
    launchButton.setFont(Font.font("Satoshi", 20));

    container.setCenter(innerLobby);
    container.setTop(backButton);
    container.setBottom(new HBox(30, readyButton, launchButton));
    container.setPadding(new Insets(30, 30, 30, 30));

    readyButton.setOnAction(evt -> {

    });

    launchButton.setOnAction(evt -> {

    });
  }*/

  @FXML
  private void handleAddLobby() {
    Lobby lobby = null;

    try {
      lobby = new Lobby(lobbyImgs[new Random().nextInt(4)], 3, com.hexanome.fourteen.boards.Expansion.ORIENT, lobbyHosts[new Random().nextInt(5)], this);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    if (lobby != null) {
      lobbyVBox.getChildren().add(lobby);
    }
  }
}
