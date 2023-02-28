package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.form.lobbyservice.CreateSessionForm;
import com.hexanome.fourteen.form.lobbyservice.SaveGameForm;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoadGameScreenController implements ScreenController{

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Button backButton;
  @FXML
  private ScrollPane savedGamesScrollView;
  @FXML
  private VBox savedGamesVBox;
  @FXML
  private ToggleButton saveGameOneToggle;
  @FXML
  private ToggleButton saveGameTwoToggle;
  @FXML
  private final ToggleGroup loadSetting = new ToggleGroup();
  @FXML
  private Button createLobbyButton;


  private List<SaveGameForm> savedGames;
  private Stage stage;

  @Override
  public void sendStageData(Stage stage) {
    this.stage = stage;

    // Post init
    //Initialize ToggleGroup and Toggles for selecting a save game in the load game menge
    ArrayList<ToggleButton> loadToggles =
            new ArrayList<>(Arrays.asList(saveGameOneToggle, saveGameTwoToggle));
    for (Toggle toggle : loadToggles) {
      toggle.setToggleGroup(loadSetting);
    }

    getSavedGames();
  }

  @FXML
  public void handleCreateLobbyButton() {
    /*try {
      System.out.println("Loaded game: "+loadSetting.getSelectedToggle().toString());
      MenuController.getMenuController(stage).goToInLobbyScreen(null);
    } catch (Exception e) {
      e.printStackTrace();
    }*/


    System.out.println("Loading game:" +  loadSetting.getSelectedToggle().getUserData());

    // TODO: Add player to existing lobby if sessions already exists, otherwise create it.

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
      MenuController.goBack();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void getSavedGames() {

    LobbyServiceCaller.updateAccessToken();
    savedGamesVBox.getChildren().clear();
    savedGames = LobbyServiceCaller.getSavedGames();

    if (savedGames != null) {
      for (SaveGameForm sg : savedGames){
        System.out.println(sg.saveGameid());
        DisplaySavedGame displayGame = null;

        try {
          displayGame = new DisplaySavedGame(sg, this);
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }

        if (displayGame != null) {
          displayGame.setToggleGroup(loadSetting);
          savedGamesVBox.getChildren().add(displayGame);
        }

      }
    }

  }
}
