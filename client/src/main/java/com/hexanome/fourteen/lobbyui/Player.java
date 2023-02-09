package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.LobbyServiceCaller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Player {

  private String username;
  private String preferredColor;

  public Player(String username, String preferredColor){
    if(username.equals(LobbyServiceCaller.getUserID())){
      this.username = ""+username;
    } else{
      this.preferredColor = ""+preferredColor;
    }
  }

  public String getUsername() {
    return username;
  }

  public String getPreferredColor() {
    return preferredColor;
  }
}
