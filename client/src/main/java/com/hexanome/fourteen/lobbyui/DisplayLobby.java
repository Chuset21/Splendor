package com.hexanome.fourteen.lobbyui;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DisplayLobby extends Pane {

  @FXML private ImageView imageDisplay;
  @FXML private Text nameText;
  @FXML private Text capacityText;
  @FXML private Text expansionText;
  @FXML private Button joinLobbyButton;

  /*ImageView image;
  Text name;
  int curPlayers;
  int capacity;
  Text capacityDisplay;
  Text expansion;
  Button joinButton;*/

  public DisplayLobby(/*String fileString, String name, int curPlayers, */) {
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(DisplayLobby.class.getResource("defaultLobby.fxml")));
    loader.setRoot(this);
    loader.setController(this);
    try{
      loader.load();
    } catch(IOException ioe){
      ioe.printStackTrace();
    }


    /*ImageView image = new ImageView(new Image(DisplayLobby.class.getResource("images/"+fileString).toString()));
    this.name = new Text(name);
    this.curPlayers = curPlayers;
    this.capacity = capacity;
    this.capacityDisplay = new Text(curPlayers+"/"+capacity);
    this.expansion = new Text(expansion.toString());
    joinButton = new Button();*/

    //this.getChildren().add(image);

  }
}
