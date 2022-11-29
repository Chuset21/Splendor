package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.form.lobbyservice.GameParametersForm;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;
import com.hexanome.fourteen.form.lobbyservice.SessionsForm;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LobbyController implements Initializable {
  private static Stage aPrimaryStage;
  private static Stack<Scene> scenePath = new Stack<>();
  private static String username;

  // Temp items to use for making lobbies
  private static final String[] lobbyImgs = {"cat.jpg","dog.jpg","squirrel.jpg","chameleon.jpg"};
  private static final String[] lobbyHosts = {"Billy Bob", "John Smith", "Gerald", "Betsy", "Brenda"};

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Text welcomeText;
  @FXML
  private Button newGameButton;
  @FXML
  private Button joinGameButton;
  @FXML
  private Button createGameButton;
  @FXML
  private Button quitButton;
  @FXML
  private Pane stdOpacityPane;
  @FXML
  private Button backButton;
  @FXML
  private TextField createLobbyPasswordField;
  @FXML
  private ToggleButton selectOrientToggle = new ToggleButton();
  @FXML
  private ToggleButton selectExtraToggle = new ToggleButton();
  @FXML
  private ToggleButton maxPlayerToggleTwo = new ToggleButton();
  @FXML
  private ToggleButton maxPlayerToggleThree = new ToggleButton();
  @FXML
  private ToggleButton maxPlayerToggleFour = new ToggleButton();
  @FXML
  private Button createLobbyButton;
  @FXML
  private ToggleButton saveGameOneToggle;
  @FXML
  private ToggleButton saveGameTwoToggle;
  @FXML
  private Button joinLobbyButton;
  @FXML
  private Button fullLobbyButton;
  @FXML
  private Button readyUpButton;
  @FXML
  private Button leaveLobbyButton;
  @FXML
  private ImageView playerOneAvatar;
  @FXML
  private ImageView playerTwoAvatar;
  @FXML
  private ImageView playerThreeAvatar;
  @FXML
  private ImageView playerFourAvatar;
  @FXML
  private Text playerOneReadyText;
  @FXML
  private Text playerTwoReadyText;
  @FXML
  private Text playerThreeReadyText;
  @FXML
  private Text playerFourReadyText;
  @FXML
  private final ToggleGroup maxPlayersSetting = new ToggleGroup();
  @FXML
  private final ToggleGroup expansionSetting = new ToggleGroup();
  @FXML
  private final ToggleGroup loadSetting = new ToggleGroup();
  @FXML
  private ScrollPane lobbyScrollView;
  @FXML
  private VBox lobbyVBox;
  @FXML
  private Pane defaultLobby;
  @FXML
  private Button addLobby;
  @FXML
  private GridPane lobbyGrid;
  @FXML
  private HBox defaultPlayer;
  @FXML
  private Text displayUsername;


  private final AnchorPane innerLobby = new AnchorPane();
  private final GridPane playerGrid = new GridPane();
  private final Button readyButton = new Button("Ready");
  private final Button launchButton = new Button("Launch");

  private ArrayList<String> users = new ArrayList<>();
  private Text playerOne = new Text();
  private Text playerTwo = new Text();
  private boolean isPlayerOneReady = false;
  private boolean isPlayerTwoReady = false;
  private BorderPane container = new BorderPane();
  private boolean dupliCheck = false;


  public void goToChoiceSelect(Stage pStage, String username) throws IOException {
    aPrimaryStage = pStage;
    LobbyController.username = username;

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
        Objects.requireNonNull(LobbyController.class.getResource("choiceSelect.fxml")));
    // Import root from fxml file
    Parent root = loader.load();
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
    // Initialize welcome message with the current player's username
    if(displayUsername != null) {
      displayUsername.setText(username);
    }
  }

  public void initLobbySelect(){
    SessionsForm lobbyform = LobbyServiceCaller.getSessions();
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
    }

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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    init();

    if(lobbyVBox != null){
      initLobbySelect();
    }

    //Initialize ToggleGroup and Toggles for selecting max players in the create game menu
    ArrayList<ToggleButton> maxPlayersToggles = new ArrayList<>(
        Arrays.asList(maxPlayerToggleTwo, maxPlayerToggleThree, maxPlayerToggleFour));
    for (Toggle toggle : maxPlayersToggles) {
      toggle.setToggleGroup(maxPlayersSetting);
    }

    //Initialize ToggleGroup and Toggles for selecting an expansion in the create game menu
    ArrayList<ToggleButton> expansionToggles =
        new ArrayList<>(Arrays.asList(selectExtraToggle, selectOrientToggle));
    for (Toggle toggle : expansionToggles) {
      toggle.setToggleGroup(expansionSetting);
    }
    System.out.println("init");
  }

  public void loadScene(Parent root) {
    //Push the current scene to the scene stack
    scenePath.push(aPrimaryStage.getScene());
    //Load Scene
    Scene scene = new Scene(root);
    //Set Scene Styling
    scene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());
    //Set and Display Scene
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
      aPrimaryStage.hide();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Goes to the lobby selection screen
   */
  public void handleJoinGameButton() {
    //Switch scene to join game details (lobby selection)
    try {
      Parent root = FXMLLoader.load(getClass().getResource("joinGame.fxml"));
      loadScene(root);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void handleLoadGameButton() {
    //Switch scene to load game details (chose game save)
    try {
      Parent root = FXMLLoader.load(getClass().getResource("loadGame.fxml"));
      loadScene(root);
    } catch (Exception e) {
      e.printStackTrace();
    }

    //Initialize ToggleGroup and Toggles for selecting a save game in the load game menu
    ArrayList<ToggleButton> loadToggles =
        new ArrayList<>(Arrays.asList(saveGameOneToggle, saveGameTwoToggle));
    for (Toggle toggle : loadToggles) {
      toggle.setToggleGroup(loadSetting);
    }
  }

  public void handleQuitButton() {
    Platform.exit();
    System.exit(0);
  }

  public void handleJoinLobbyButton() {
//    try {
//      Parent root = FXMLLoader.load(getClass().getResource("lobby.fxml"));
//      loadScene(root);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }

    try {
      //Push the current scene to the scene stack
      scenePath.push(aPrimaryStage.getScene());

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
      aPrimaryStage.setScene(scene);
      aPrimaryStage.show();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

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
  }

  public String getJavaFXControlName(String toStringResult) {
    int end = toStringResult.indexOf("]") + 1;
    return toStringResult.substring(end).replaceAll("'", "");
  }

  public void handleCreateLobbyButton() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("lobby.fxml"));
      loadScene(root);
    } catch (Exception e) {
      e.printStackTrace();
    }

    String selectedExpansion =
        getJavaFXControlName(expansionSetting.getSelectedToggle().toString());
    String selectedMaxPlayers =
        getJavaFXControlName(maxPlayersSetting.getSelectedToggle().toString());

    String[] lobbyData = new String[] {selectedExpansion, selectedMaxPlayers};
    System.out.println(Arrays.toString(lobbyData));
  }

  public void handleGameSaveToggle(MouseEvent event) {
    SavedGame selectedSave = new SavedGame(event.getSource());
    System.out.println(selectedSave.getGameName());
  }

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
