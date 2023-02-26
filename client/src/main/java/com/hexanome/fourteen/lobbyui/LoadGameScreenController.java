package com.hexanome.fourteen.lobbyui;

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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoadGameScreenController implements ScreenController{

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Button backButton;
  @FXML
  private ToggleButton saveGameOneToggle;
  @FXML
  private ToggleButton saveGameTwoToggle;
  @FXML
  private final ToggleGroup loadSetting = new ToggleGroup();
  @FXML
  private Button createLobbyButton;

  private Stage stage;

  @Override
  public void sendStageData(Stage stage) {
    this.stage = stage;

    // Post init
    //Initialize ToggleGroup and Toggles for selecting a save game in the load game menu
    ArrayList<ToggleButton> loadToggles =
            new ArrayList<>(Arrays.asList(saveGameOneToggle, saveGameTwoToggle));
    for (Toggle toggle : loadToggles) {
      toggle.setToggleGroup(loadSetting);
    }
  }

  @FXML
  public void handleCreateLobbyButton() {
    try {
      System.out.println("Loaded game: "+loadSetting.getSelectedToggle().toString());
      MenuController.getMenuController(stage).goToInLobbyScreen(null);
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
      MenuController.getMenuController(stage).goBack();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
