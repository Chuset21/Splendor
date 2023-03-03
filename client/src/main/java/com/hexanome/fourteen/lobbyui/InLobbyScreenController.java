package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.ServerCaller;
import com.hexanome.fourteen.form.server.GameBoardForm;
import java.io.IOException;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.TokenRefreshFailedException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InLobbyScreenController implements ScreenController {

  //TODO: Add "Waiting for player" text in empty player spots

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Text titleText;
  @FXML
  private Text capacityText;
  @FXML
  private GridPane lobbyGrid;
  @FXML
  private Button launchLobbyButton;
  @FXML
  private Button joinLobbyButton;
  @FXML
  private Button leaveLobbyButton;

  private Thread refresherThread;
  // Holds data of current lobby (primarily the lobby location)
  private Lobby lobby;


  // List of current players
  private DisplayPlayer[] displayPlayers = new DisplayPlayer[4];

  // Temp names to use for players
  private static final String[] playerNames =
      {"Billy Bob", "John Smith", "Gerald", "Betsy", "Brenda"};

  // Template for lobby text
  private static final String LOBBY_NAME_TEMPLATE = "[ownerName]'s Lobby";
  private static final String PLAYER_COUNT_TEMPLATE = "[curPlayers]/[maxPlayers] Players";

  private Stage stage;

  @Override
  public void sendStageData(Stage stage) {
    this.stage = stage;

    // Get data from MenuController
    this.lobby = LobbyServiceCaller.getCurrentUserLobby();

    // Sets lobby info to automatically refresh
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {

        while (!Thread.currentThread().isInterrupted()) {
          Platform.runLater(() -> {
            LobbyServiceCaller.updateAccessToken();
            updateLobbyInfo();
          });
        }
        return null;
      }
    };

    refresherThread = new Thread(task);
    refresherThread.setDaemon(true);
    refresherThread.start();

    joinLobbyButton.setVisible(false);

    // TODO: remove launch button when not the host (make it into a join button?)
    if (!LobbyServiceCaller.getCurrentUserLobby().getHost()
        .equals(LobbyServiceCaller.getCurrentUserid())) {
      launchLobbyButton.setVisible(false);
    }
  }

  @FXML
  private void handleLaunchButton() {
    if (LobbyServiceCaller.getCurrentUserid().equals(lobby.getHost())) {
      try {
        if (LobbyServiceCaller.launchSession()) {
          refresherThread.interrupt();

          // Go to board screen
          MenuController.goToGameBoard();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      try {
        refresherThread.interrupt();

        // Go to board screen
        MenuController.goToGameBoard();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void handleJoinButton() {

  }

  @FXML
  private void handleLeaveButton() {
    if (LobbyServiceCaller.getCurrentUserid().equals(lobby.getHost())) {
      try {
        LobbyServiceCaller.deleteSession();
      } catch (TokenRefreshFailedException e) {
        try {
          MenuController.returnToLogin("Session timed out, retry login");
          stage.close();
          return;
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    } else {
      try {
        LobbyServiceCaller.leaveSession();
      } catch (TokenRefreshFailedException e) {
        try {
          MenuController.returnToLogin("Session timed out, retry login");
          stage.close();
          return;
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    }

    try {
      MenuController.goBack();
      refresherThread.interrupt();
      LobbyServiceCaller.setCurrentUserLobby(null);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * LEGACY
   * Adds a player to the players list and displays them in the lobby
   *
   * @param displayPlayer player to be added
   * @return true if player was added successfully, false if no room was left in lobby
   */
  private boolean addPlayer(DisplayPlayer displayPlayer) {
    for (int i = 0; i < 4; i++) {
      if (displayPlayers[i] == null) {
        displayPlayers[i] = displayPlayer;
        lobbyGrid.add(displayPlayer, i / 2, i % 2);
        return true;
      }
    }
    return false;
  }

  /**
   * Updates all lobby information, kicks player from lobby if session is no longer active
   */
  private void updateLobbyInfo() {
    if (!LobbyServiceCaller.isSessionActive(lobby.getSessionid())) {
      handleLeaveButton();
      return;
    }

    lobby.updateLobby();

    updateGUI();
    updateLobbyName();
    updatePlayerCounter();
    updateLobbyPlayers();
  }

  /**
   * Updates GUI elements to match gamestate
   */
  private void updateGUI() {
    // Updates joinLobby buttons for non-host users
    if (LobbyServiceCaller.getCurrentUserid().equals(lobby.getHost()) || lobby.getLaunched()) {
      joinLobbyButton.setVisible(false);
      launchLobbyButton.setVisible(true);
    } else {
      joinLobbyButton.setVisible(false);
      launchLobbyButton.setVisible(false);
    }
  }

  /**
   * Updates display of players currently in the lobby
   */
  private void updateLobbyPlayers() {
    lobbyGrid.getChildren().clear();

    for (int i = 0; i < lobby.getNumPlayers(); i++) {
      DisplayPlayer player = null;

      try {
        player = new DisplayPlayer(new Player(lobby.getPlayers()[i], "Green"), this);
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      if (player != null) {
        lobbyGrid.add(player, i / 2, i % 2);
      }
    }
  }

  /**
   * Displays the lobby's owner name as the lobby owner
   */
  private void updateLobbyName() {
    titleText.setText(LOBBY_NAME_TEMPLATE.replace("[ownerName]", lobby.getPlayers()[0]));
  }

  /**
   * Updates player count with gamestate
   */
  private void updatePlayerCounter() {
    String temp = PLAYER_COUNT_TEMPLATE.replace("[curPlayers]", "" + lobby.getNumPlayers());
    capacityText.setText(temp.replace("[maxPlayers]", "" + lobby.getPlayers().length));
  }
}
