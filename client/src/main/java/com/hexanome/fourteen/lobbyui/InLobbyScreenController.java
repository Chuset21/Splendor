package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.ServerCaller;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;
import com.hexanome.fourteen.form.server.GameBoardForm;
import java.io.IOException;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.TokenRefreshFailedException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kong.unirest.HttpResponse;
import org.apache.commons.codec.digest.DigestUtils;

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

  private Service<Void> service;
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
    service = new Service<>() {
      @Override
      protected Task<Void> createTask() {
        return new Task<>() {
          final String sessionid = LobbyServiceCaller.getCurrentUserLobby().getSessionid();

          @Override
          protected Void call() {
            HttpResponse<String> longPollResponse = null;
            String hashedResponse = null;
            while (true) {
              int responseCode = 408;
              while (responseCode == 408) {
                longPollResponse =
                    hashedResponse != null
                        ? LobbyServiceCaller.getSessionDetails(sessionid, hashedResponse) :
                        LobbyServiceCaller.getSessionDetails(sessionid);

                responseCode = longPollResponse.getStatus();
              }

              if (responseCode == 200) {
                hashedResponse = DigestUtils.md5Hex(longPollResponse.getBody());
                final SessionForm session =
                    Main.GSON.fromJson(longPollResponse.getBody(), SessionForm.class);
                Platform.runLater(() -> {
                  lobby.setSession(session);
                  updateLobbyInfo();
                });
              } else {
                LobbyServiceCaller.updateAccessToken();
              }
            }
          }
        };
      }
    };
    service.start();

    joinLobbyButton.setVisible(false);

    updateLobbyInfo();
  }

  @FXML
  private void handleLaunchButton() {
    if (LobbyServiceCaller.getCurrentUserid().equals(lobby.getHost())) {
      try {
        if (LobbyServiceCaller.launchSession()) {
          service.cancel();

          // Go to board screen
          MenuController.goToGameBoard();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      try {
        service.cancel();

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
      service.cancel();
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
    if ((LobbyServiceCaller.getCurrentUserid().equals(lobby.getHost()) && lobby.getNumPlayers() > 1) || lobby.getLaunched()) {
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
