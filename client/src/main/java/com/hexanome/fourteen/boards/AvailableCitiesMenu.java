package com.hexanome.fourteen.boards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class AvailableCitiesMenu extends DialogPane {

  @FXML
  Label titleLabel;

  @FXML
  ImageView availableCity0;


  @FXML
  ImageView availableCity1;

  @FXML
  ImageView availableCity2;



  final GameBoard gameBoard;


  public AvailableCitiesMenu(GameBoard gameBoard) throws IOException {
    super();

    // Load basic lobby UI
    FXMLLoader loader =
        new FXMLLoader(
            Objects.requireNonNull(TradingPostsMenu.class.getResource("AvailableCities.fxml")));

    // Apply the UI to this class (set this object as the root)
    loader.setRoot(this);

    // THIS IS IMPORTANT, you can't set this object as the root unless the FXML file HAS NO CONTROLLER SET & it has fx:root="Pane"
    loader.setController(this);
    loader.load();

    this.gameBoard = gameBoard;

    // Update our available cities
    updateCities();
  }

  public void updateCities() {

    ArrayList<City> availableCities = City.interpretCities(gameBoard.getGameBoardForm());

    availableCity0.setImage(availableCities.get(0));

    availableCity1.setImage(availableCities.get(1));

    availableCity2.setImage(availableCities.get(2));

  }


}
