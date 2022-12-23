package com.hexanome.fourteen.lobbyui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Lobby extends Pane implements Initializable {

  @FXML private ImageView image;
  @FXML private Text name;
  @FXML private Text capacityText;
  @FXML private Text expansionText;
  @FXML private Button joinLobbyButton;

  private int numPlayers;
  private String[] players;
  private com.hexanome.fourteen.boards.Expansion expansion;
  private LobbySelectScreenController controller;

  public Lobby(String fileString, int capacity, com.hexanome.fourteen.boards.Expansion expansion, String host, LobbySelectScreenController controller)
      throws IOException {

    // Load basic lobby UI
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Lobby.class.getResource("defaultLobby.fxml")));

    // Apply the UI to this class (set this object as the root)
    loader.setRoot(this);

    // THIS IS IMPORTANT, you can't set this object as the root unless the FXML file HAS NO CONTROLLER SET & it has fx:root="Pane"
    loader.setController(this);
    loader.load();

    // Initialize object vars
    image.setImage(new Image(Objects.requireNonNull(Lobby.class.getResource("images/"+fileString).toString())));

    // Set player list
    this.numPlayers = 1;
    this.players = new String[capacity];
    players[0] = host;

    // Set expansion
    this.expansion = expansion;

    // Set lobby name
    name.setText(players[0] + "'s Lobby");

    // Set text describing lobby
    capacityText.setText(numPlayers +"/"+ players.length);
    expansionText.setText(expansion.toString());

    // Set instance of LobbyController this was created by
    this.controller = controller;

    /**
     * Calls handleJoinLobbyButton() from lobby controller class on button click
     */
    joinLobbyButton.setOnAction(e -> {
      if(numPlayers < players.length){
        controller.handleJoinLobbyButton();
      }
    });

    for(Node n : this.getChildren()){
      System.out.println(n);
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }

}
