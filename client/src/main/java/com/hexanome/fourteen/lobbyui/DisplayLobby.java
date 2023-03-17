package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.GameServiceName;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DisplayLobby extends Pane implements Initializable {

  @FXML
  private ImageView image;
  @FXML
  private Text name;
  @FXML
  private Text capacityText;
  @FXML
  private Text expansionText;
  @FXML
  private Button joinLobbyButton;

  private final Lobby lobby;
  private final LobbySelectScreenController controller;

  // Temp items to use for making lobbies
  private static final String[] lobbyImgs = {"cat.jpg", "dog.jpg", "squirrel.jpg", "chameleon.jpg"};

  public DisplayLobby(Lobby lobby, LobbySelectScreenController controller)
      throws IOException {
    // Load basic lobby UI
    FXMLLoader loader =
        new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("defaultLobby.fxml")));

    // Apply the UI to this class (set this object as the root)
    loader.setRoot(this);

    // THIS IS IMPORTANT, you can't set this object as the root unless the FXML file HAS NO CONTROLLER SET & it has fx:root="Pane"
    loader.setController(this);
    loader.load();

    // Set lobby
    this.lobby = lobby;

    // Set lobby image
    String img = lobbyImgs[new Random().nextInt(4)];
    image.setImage(
        new Image(Objects.requireNonNull(
            Objects.requireNonNull(Lobby.class.getResource("images/" + img)).toString())));

    // Set lobby name
    name.setText(lobby.getPlayers()[0] + "'s Lobby");

    // Set text describing lobby
    capacityText.setText(lobby.getNumPlayers() + "/" + lobby.getPlayers().length);
    expansionText.setText(
        String.join(", ", GameServiceName.getExpansions(lobby.getGameServiceName()).stream()
            .map(Enum::toString).collect(
                Collectors.toSet())).replace("_", " "));

    // Set instance of LobbyController this was created by
    this.controller = controller;

    /**
     * Calls handleJoinLobbyButton() from lobby controller class on button click
     */
    joinLobbyButton.setOnAction(e -> {
      if (lobby.getNumPlayers() < lobby.getPlayers().length) {
        controller.handleJoinLobbyButton(this.lobby);
      }
    });
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }
}
