package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.GameServiceName;
import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.form.lobbyservice.CreateSessionForm;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateGameScreenController implements ScreenController {

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Button backButton;
  @FXML
  private ToggleButton selectCitiesToggle;
  @FXML
  private ToggleButton selectTradeRoutesToggle;
  @FXML
  private Button createLobbyButton;
  private List<ToggleButton> expansionToggles;
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
    expansionToggles = Arrays.asList(selectTradeRoutesToggle, selectCitiesToggle);

    // Set toggle values to their respective enums
    selectCitiesToggle.setUserData(Expansion.CITIES);
    selectTradeRoutesToggle.setUserData(Expansion.TRADING_POSTS);
  }

  /**
   * Sends request to create lobby to lobbyservice. Requested lobby has parameters designated by
   * the user.
   */
  @FXML
  public void handleCreateLobbyButton() {
    final Set<Expansion> expansions =
        expansionToggles.stream().filter(ToggleButton::isSelected)
            .map(n -> (Expansion) n.getUserData()).collect(Collectors.toSet());
    expansions.add(Expansion.STANDARD);
    expansions.add(Expansion.ORIENT);

    // Create template for session with current user's ID and the selected expansion
    CreateSessionForm session = new CreateSessionForm(LobbyServiceCaller.getCurrentUserid(),
        GameServiceName.getGameServiceName(expansions).toString());

    // Gets sessions
    String sessionid = null;

    sessionid = LobbyServiceCaller.createSession(session);

    System.out.println("SessionID: " + sessionid);

    if (sessionid != null) {
      try {
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
