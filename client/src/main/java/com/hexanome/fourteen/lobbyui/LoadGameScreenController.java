package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.form.lobbyservice.CreateSessionForm;
import com.hexanome.fourteen.form.lobbyservice.SaveGameForm;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoadGameScreenController implements ScreenController {

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

  private Map<String, SaveGameForm> savedGames;
  private Stage stage;

  @Override
  public void sendStageData(Stage stage) {
    this.stage = stage;

    // Post init
    savedGames = new HashMap<String, SaveGameForm>();

    // display our saved games
    displaySavedGames();


  }

  @FXML
  public void handleCreateLobbyButton() {
    // Information Variables about our session/savegame
    String sessionID = null;
    String saveGameID = (String) loadSetting.getSelectedToggle().getUserData();

    // Print to console our next step
    System.out.println("Loading game:" + saveGameID);

    // Creates template for session with current user's ID and the selected expansion
    CreateSessionForm session = new CreateSessionForm(LobbyServiceCaller.getCurrentUserid(),
        savedGames.get(saveGameID).gameName(), saveGameID);

    // Get the session
    sessionID = LobbyServiceCaller.createSession(session);

    System.out.println("Session ID: " + sessionID);

    // Create the lobby for the session
    if (sessionID != null) {
      try {
        // Go to Lobby
        MenuController.goToInLobbyScreen(new Lobby(sessionID));
      } catch (IOException ioe) {
        LobbyServiceCaller.setCurrentUserLobby(null);
        ioe.printStackTrace();
      }
    }
  }

  @FXML
  private void handleBackButton() {
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
    List<SaveGameForm> savedGamesList =
        LobbyServiceCaller.getSavedGames().stream().filter(e -> e.players().contains(username))
            .toList();

    for (SaveGameForm sg : savedGamesList) {
      // Add our saved game to our list
      savedGames.put(sg.saveGameid(), sg);

      // Make a toggle button for the game
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
