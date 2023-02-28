package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class DisplayPlayer extends HBox implements Initializable {

  @FXML private ImageView playerAvatar;
  @FXML private Text playerName;
  private Player player;
  private boolean isYou;

  private InLobbyScreenController controller;

  // Temp items to use for making lobbies
  private static final String[] playerImgs = {"cat.jpg","dog.jpg","squirrel.jpg","chameleon.jpg"};

  public DisplayPlayer(Player player, InLobbyScreenController controller)
      throws IOException {
    this.player = player;
    this.isYou = player.getUsername().equals(LobbyServiceCaller.getCurrentUserid());

    // Load basic lobby UI
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("defaultPlayer.fxml")));

    // Apply the UI to this class (set this object as the root)
    loader.setRoot(this);

    // THIS IS IMPORTANT, you can't set this object as the root unless the FXML file HAS NO CONTROLLER SET & it has fx:root="Pane"
    loader.setController(this);
    loader.load();

    // Initialize object vars
    playerAvatar.setImage(new Image(Objects.requireNonNull(Lobby.class.getResource("images/"+playerImgs[new Random().nextInt(4)]).toString())));

    // Set lobby name
    this.playerName.setText(player.getUsername() + ((isYou) ? " (you)":""));

    this.controller = controller;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }
}
