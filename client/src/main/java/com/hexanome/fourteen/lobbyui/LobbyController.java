package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.boards.Expansions;
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

  // To replace <username> with username, use .replaceAll()
  private static final String welcomeTextTemplate = "Welcome back <username>!";


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


  private final AnchorPane innerLobby = new AnchorPane();
  private final GridPane playerGrid = new GridPane();
  private final Button readyButton = new Button("Ready");
  private ArrayList<String> users = new ArrayList<>();
  private Text playerOne = new Text();
  private Text playerTwo = new Text();
  private Text playerThree = new Text();
  private Text playerFour = new Text();


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
    if (welcomeText != null) {
      // Initialize welcome message with username
      welcomeText.setText(welcomeTextTemplate.replaceAll("<username>", username));
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    init();

    //Initialize ToggleGroup and Toggles for selecting max players in the create game menu
    ArrayList<ToggleButton> maxPlayersToggles = new ArrayList<ToggleButton>(
        Arrays.asList(maxPlayerToggleTwo, maxPlayerToggleThree, maxPlayerToggleFour));
    for (Toggle toggle : maxPlayersToggles) {
      toggle.setToggleGroup(maxPlayersSetting);
    }

    //Initialize ToggleGroup and Toggles for selecting an expansion in the create game menu
    ArrayList<ToggleButton> expansionToggles =
        new ArrayList<ToggleButton>(Arrays.asList(selectExtraToggle, selectOrientToggle));
    for (Toggle toggle : expansionToggles) {
      toggle.setToggleGroup(expansionSetting);
    }
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

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

      BorderPane container = new BorderPane();

      //Create, style, and display scene
      Scene scene = new Scene(innerLobby);
      aPrimaryStage.setScene(scene);
      aPrimaryStage.show();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private void initInnerLobby() {

    playerOne = new Text("Waiting for player...");
    playerOne.setFont(Font.font ("Satoshi", 20));

    playerTwo = new Text("Waiting for player...");
    playerTwo.setFont(Font.font ("Satoshi", 20));

    playerGrid.add(playerOne, 0, 0);
    playerGrid.add(playerTwo, 1, 0);

    innerLobby.getChildren().add(playerGrid);
    playerGrid.setPrefSize(1200, 700);
    playerGrid.setPadding(new Insets(80, 20, 20, 20));
    playerGrid.setHgap(20);
    playerGrid.setVgap(20);

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
      lobby = new Lobby("cat.jpg", 3, Expansions.ORIENT, username);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    if(lobby != null){
      lobbyVBox.getChildren().add(lobby);
    }
  }
}
