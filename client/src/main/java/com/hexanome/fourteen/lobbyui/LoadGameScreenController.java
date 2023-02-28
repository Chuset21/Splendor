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
  private final ToggleGroup loadSetting = new ToggleGroup();
  @FXML
  private Button createLobbyButton;


  private List<SaveGameForm> savedGames;
  private Stage stage;

  @Override
  public void sendStageData(Stage stage) {
    this.stage = stage;

    // Post init

    // Update our access tokens on page load
    LobbyServiceCaller.updateAccessToken();

    // display our saved games
    displaySavedGames();


  }

  @FXML
  public void handleCreateLobbyButton() {
    /*try {
      System.out.println("Loaded game: "+loadSetting.getSelectedToggle().toString());
      MenuController.getMenuController(stage).goToInLobbyScreen(null);
    } catch (Exception e) {
      e.printStackTrace();
    }*/

    /*String selectedExpansion =
        getJavaFXControlName(expansionSetting.getSelectedToggle().toString());
    String selectedMaxPlayers =
        getJavaFXControlName(maxPlayersSetting.getSelectedToggle().toString());

    String[] lobbyData = new String[] {selectedExpansion, selectedMaxPlayers};
    System.out.println(Arrays.toString(lobbyData));*/

    System.out.println("Loading game:" +  loadSetting.getSelectedToggle().getUserData());

    // TODO: Add player to existing lobby if sessions already exists, otherwise create it.

    // TODO: Implement putting player in correct lobby once created

  }

  @FXML
  private void handleBackButton(){
    try {
      MenuController.goBack();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void displaySavedGames() {

    // Clear the VBox
    savedGamesVBox.getChildren().clear();

    // Only load the games that has the user included
    String username = LobbyServiceCaller.getCurrentUserid();
    System.out.println(username);
    savedGames = LobbyServiceCaller.getSavedGames().stream().filter(e-> e.players().contains(username)).toList();

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
