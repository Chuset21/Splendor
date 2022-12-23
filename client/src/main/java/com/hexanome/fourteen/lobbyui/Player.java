package com.hexanome.fourteen.lobbyui;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Player extends HBox {

  @FXML private ImageView playerAvatar;
  @FXML private Text playerName;

  private InLobbyScreenController controller;

  public Player(String avatarFile, String playerName, InLobbyScreenController controller)
      throws IOException {

    // Load basic lobby UI
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("defaultLobby.fxml")));

    // Apply the UI to this class (set this object as the root)
    loader.setRoot(this);

    // THIS IS IMPORTANT, you can't set this object as the root unless the FXML file HAS NO CONTROLLER SET & it has fx:root="Pane"
    loader.setController(this);
    loader.load();

    // Initialize object vars
    playerAvatar.setImage(new Image(Objects.requireNonNull(Lobby.class.getResource("images/"+avatarFile).toString())));

    // Set lobby name
    this.playerName.setText(playerName);

    this.controller = controller;
  }

}
