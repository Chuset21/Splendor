package com.hexanome.fourteen.lobbyui;

import java.io.IOException;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public interface ScreenController extends Initializable {

  public void goTo(Stage stage) throws IOException;

}
