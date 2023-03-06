package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.GameServiceName;
import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.TokenRefreshFailedException;
import com.hexanome.fourteen.form.lobbyservice.CreateSessionForm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateGameScreenController implements ScreenController {

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Button backButton;
  @FXML
  private ToggleButton selectOrientToggle;
  @FXML
  private ToggleButton selectExtraToggle;
  @FXML
  private final ToggleGroup expansionSetting = new ToggleGroup();
  @FXML
  private Button createLobbyButton;

  private Stage stage;

  /**
   * Initializes the screen
   *
   * @param stage where the screen will be displayed
   * @throws IOException thrown when FXML loader fails to load screen's .fxml file
   */
  @Override
  public void sendStageData(Stage stage) {
    this.stage = stage;

    // Post init
    //Initialize ToggleGroup and Toggles for selecting an expansion in the create game menu
    ArrayList<ToggleButton> expansionToggles =
        new ArrayList<>(Arrays.asList(selectExtraToggle, selectOrientToggle));
    for (Toggle toggle : expansionToggles) {
      toggle.setToggleGroup(expansionSetting);
    }

    // Set toggle values to their respective enums
    selectOrientToggle.setUserData(GameServiceName.BASE);
    selectExtraToggle.setUserData(GameServiceName.ALL);
  }

  /**
   * Sends request to create lobby to lobbyservice. Requested lobby has parameters designated by
   * the user.
   */
  @FXML
  public void handleCreateLobbyButton() {
    // Create template for session with current user's ID and the selected expansion
    CreateSessionForm session = new CreateSessionForm(LobbyServiceCaller.getCurrentUserid(),
        (expansionSetting.getSelectedToggle().getUserData()).toString());

    // Gets sessions
    String sessionid = null;

    try {
      sessionid = LobbyServiceCaller.createSession(session);
    } catch (TokenRefreshFailedException e) {
      try {
        MenuController.returnToLogin("Session timed out, retry login");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    System.out.println("SessionID: " + sessionid);

    if (sessionid != null) {
      try {
        System.out.println(
            "Expansion toggle: " + (expansionSetting.getSelectedToggle().getUserData()).toString());
        MenuController.goToInLobbyScreen(new Lobby(sessionid));
      } catch (Exception ioe) {
        LobbyServiceCaller.setCurrentUserLobby(null);
        ioe.printStackTrace();
      }
    }
  }

  /**
   * Sends user to the previous screen
   */
  @FXML
  private void handleBackButton() {
    try {
      MenuController.goBack();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
