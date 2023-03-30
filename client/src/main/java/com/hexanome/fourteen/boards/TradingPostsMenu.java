package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.PlayerForm;
import com.hexanome.fourteen.form.server.tradingposts.TradingPostsEnum;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class TradingPostsMenu extends DialogPane {

  @FXML
  Label titleLabel;
  @FXML
  GridPane shield0Grid;
  @FXML
  GridPane shield1Grid;
  @FXML
  GridPane shield2Grid;
  @FXML
  GridPane shield3Grid;
  @FXML
  GridPane shield4Grid;
  final GameBoard gameBoard;
  final List<GridPane> shieldGrids = new ArrayList<>();

  public TradingPostsMenu(GameBoard gameBoard) throws IOException {
    super();
    // Load TradingPostsSummary.fxml
    FXMLLoader loader =
        new FXMLLoader(
            Objects.requireNonNull(TradingPostsMenu.class.getResource("TradingPostsSummary.fxml")));

    // Apply the UI to this class (set this object as the root)
    loader.setRoot(this);

    // THIS IS IMPORTANT, you can't set this object as the root unless the FXML file HAS NO CONTROLLER SET & it has fx:root="Pane"
    loader.setController(this);
    loader.load();

    this.gameBoard = gameBoard;

    // Initialize list of shield grids
    shieldGrids.add(shield0Grid);
    shieldGrids.add(shield1Grid);
    shieldGrids.add(shield2Grid);
    shieldGrids.add(shield3Grid);
    shieldGrids.add(shield4Grid);

    updateMenu();
  }

  /**
   * Refreshes each player's trading post status and displays it.
   */
  public void updateMenu() {
    PlayerForm[] players = new PlayerForm[4];
    players = gameBoard.getGameBoardForm().players().toArray(players);

    for (int playerIndex = 0; playerIndex < 4; playerIndex++) {
      for (int shieldIndex = 0; shieldIndex < 5; shieldIndex++) {
        for (Node node : shieldGrids.get(shieldIndex).getChildren()) {
          final int x = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
          final int y = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;

          if (playerIndex == x + 2 * y) {
            // Set visibility of player
            node.setVisible(
                players[playerIndex] != null && players[playerIndex].hand().tradingPosts()
                    .getOrDefault(TradingPostsEnum.values()[shieldIndex], false));
          }
        }
      }
    }
  }

  public boolean toggleVisibility() {
    this.setVisible(!isVisible());
    if(isVisible()){
      toFront();
    }
    return isVisible();
  }


}
