package com.hexanome.fourteen.lobbyui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LobbyPlayerController implements Initializable {

  @FXML
  private HBox player;
  @FXML
  private ImageView avatar;
  @FXML
  private VBox info;
  @FXML
  private Text user;


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }
}
