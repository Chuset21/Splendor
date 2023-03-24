package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.form.lobbyservice.SessionForm;
import com.hexanome.fourteen.form.lobbyservice.SessionsForm;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kong.unirest.HttpResponse;
import org.apache.commons.codec.digest.DigestUtils;

public class LobbySelectScreenController implements ScreenController {

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private ScrollPane lobbyScrollView;
  @FXML
  private VBox lobbyVBox;
  @FXML
  private Button backButton;

  private Stage stage;

  private SessionsForm lobbyForm;

  private Service<Void> service;

  @Override
  public void sendStageData(Stage stage) {
    this.stage = stage;

    service = new Service<>() {
      @Override
      protected Task<Void> createTask() {
        return new Task<>() {
          @Override
          protected Void call() {
            HttpResponse<String> longPollResponse = null;
            String hashedResponse = null;
            while (true) {
              int responseCode = 408;
              while (responseCode == 408) {
                longPollResponse =
                    hashedResponse != null ? LobbyServiceCaller.getSessions(hashedResponse) :
                        LobbyServiceCaller.getSessions();

                responseCode = longPollResponse.getStatus();
              }

              if (responseCode == 200) {
                hashedResponse = DigestUtils.md5Hex(longPollResponse.getBody());
                final SessionsForm notLaunchedSessions = new SessionsForm(
                    Main.GSON.fromJson(longPollResponse.getBody(), SessionsForm.class).sessions()
                        .entrySet().stream().filter(e -> !e.getValue().launched()
                                                         || e.getValue().players().contains(
                            LobbyServiceCaller.getCurrentUserid()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                Platform.runLater(() -> {
                  lobbyForm = notLaunchedSessions;
                  updateLobbies();
                });
              }
            }
          }
        };
      }
    };
    service.start();
    // Post init
    //updateLobbies();
  }


  private void updateLobbies() {
    // Resets displayed lobbies
    lobbyVBox.getChildren().clear();
    if (lobbyForm != null) {
      Map<String, SessionForm> lobbies = lobbyForm.sessions();

      // Iterates through all active sessions
      for (Map.Entry<String, SessionForm> entry : lobbies.entrySet()) {
        // Creates new lobby based on session data from lobby service
        Lobby lobby = new Lobby(entry.getKey());
        DisplayLobby displayLobby = null;

        try {
          // Creates new display lobby based on data received from lobby service

          displayLobby = new DisplayLobby(lobby, this);
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }

        if (displayLobby != null) {
          lobbyVBox.getChildren().add(displayLobby);
        }
      }
    }
  }

  @FXML
  private void handleBackButton() {
    try {
      MenuController.goBack();
      service.cancel();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * Join given lobby
   * This function is only used when called by the Lobby object,
   * never directly from FXML.
   */
  public void handleJoinLobbyButton(Lobby lobby) {
    if (!lobby.getHost().equals(LobbyServiceCaller.getCurrentUserid())) {
      try {
        if (lobby.getLaunched() || LobbyServiceCaller.joinSession(lobby.getSessionid())) {
          MenuController.goToInLobbyScreen(lobby);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      try {
        MenuController.goToInLobbyScreen(lobby);
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
    service.cancel();
  }
}
