package com.hexanome.fourteen.lobbyui;

import java.io.IOException;
import javafx.stage.Stage;

/**
 * Used by MenuController in MenuController.goTo[controllerName]()
 */
@FunctionalInterface
public interface ScreenController {

  /**
   * Used to pass a stage object from the MenuController to a ScreenController,
   * which can then use (WindowContextData) Stage.getUserData() to get info
   * about the current session
   *
   * @param stage stage being used by the current window
   */
  public void sendStageData(Stage stage);

}
