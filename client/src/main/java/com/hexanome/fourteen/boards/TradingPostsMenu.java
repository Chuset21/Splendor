package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.tradingposts.TradingPostsEnum;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
  final HashMap<Player, ImageView[]> PLAYER_TO_SHIELD_MAP = new HashMap<>();

  public TradingPostsMenu(GameBoard gameBoard) throws IOException {
    super();
    // Load basic lobby UI
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


  }
}
