package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.form.lobbyservice.CreateSessionForm;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateGameScreenController implements ScreenController{

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Button backButton;
  @FXML
  private TextField createLobbyPasswordField;
  @FXML
  private ToggleButton selectOrientToggle;
  @FXML
  private ToggleButton selectExtraToggle;
  @FXML
  private ToggleButton maxPlayerToggleTwo;
  @FXML
  private ToggleButton maxPlayerToggleThree;
  @FXML
  private ToggleButton maxPlayerToggleFour;
  @FXML
  private final ToggleGroup maxPlayersSetting = new ToggleGroup();
  @FXML
  private final ToggleGroup expansionSetting = new ToggleGroup();
  @FXML
  private Button createLobbyButton;

  @Override
  public void goTo(Stage stage) throws IOException {

    // Create loader class
    FXMLLoader loader = new FXMLLoader(
        Objects.requireNonNull(MenuOrganizer.class.getResource("createGame.fxml")));
    // Import root from fxml file
    Parent root = loader.load();
    // Set up root on stage (window)
    Scene aScene = new Scene(root);
    aScene.getStylesheets().add(getClass().getResource("lobbyStyling.css").toExternalForm());

    // Initialize stage settings
    stage.setScene(aScene);
    stage.setTitle("Splendor - Create Game");
    stage.setResizable(false);
    stage.centerOnScreen();

    stage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
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

    // Set toggle values to their respective enums
    selectOrientToggle.setUserData(Expansion.ORIENT);
    selectExtraToggle.setUserData(Expansion.STANDARD);
  }

  @FXML
  public void handleCreateLobbyButton() {
    // Create template for session with current user's ID and the
    CreateSessionForm session = new CreateSessionForm(LobbyServiceCaller.getUserID(),(expansionSetting.getSelectedToggle().getUserData()).toString());
    System.out.println(LobbyServiceCaller.createSession(LobbyServiceCaller.getAccessToken(),session));

    try {
      System.out.println("Expansion toggle: "+(expansionSetting.getSelectedToggle().getUserData()).toString()
          +"\nPlayer count toggle: "+maxPlayersSetting.getSelectedToggle().toString());
      MenuOrganizer.goToLobbySelectScreen();
    } catch (Exception e) {
      e.printStackTrace();
    }


    // TODO: Implement putting player in correct lobby once created
    /*String selectedExpansion =
        getJavaFXControlName(expansionSetting.getSelectedToggle().toString());
    String selectedMaxPlayers =
        getJavaFXControlName(maxPlayersSetting.getSelectedToggle().toString());

    String[] lobbyData = new String[] {selectedExpansion, selectedMaxPlayers};
    System.out.println(Arrays.toString(lobbyData));*/
  }

  @FXML
  private void handleBackButton(){
    try {
      MenuOrganizer.goBack();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
