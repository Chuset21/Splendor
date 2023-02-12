package com.hexanome.fourteen.lobbyui;

import java.io.IOException;
import javafx.stage.Stage;

@FunctionalInterface
public interface ScreenController {

  public void sendStageData(Stage stage) throws IOException;

}
