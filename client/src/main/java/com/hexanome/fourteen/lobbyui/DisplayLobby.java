package com.hexanome.fourteen.lobbyui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DisplayLobby extends Pane {

  ImageView image;
  Text name;
  int curPlayers;
  int capacity;
  Text capacityDisplay;
  Text expansion;
  Button joinButton;

  public DisplayLobby(String fileString, String name, int curPlayers, int capacity, com.hexanome.fourteen.boards.Expansions expansion){
    super();

    ImageView image = new ImageView(new Image(DisplayLobby.class.getResource("images/"+fileString).toString()));
    this.name = new Text(name);
    this.curPlayers = curPlayers;
    this.capacity = capacity;
    this.capacityDisplay = new Text(curPlayers+"/"+capacity);
    this.expansion = new Text(expansion.toString());
    joinButton = new Button();

    this.getChildren().add(image);

  }
}
