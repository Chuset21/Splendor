package com.hexanome.fourteen.boards;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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
    final List<City> availableCities = City.interpretCities(gameBoard.getGameBoardForm());

    availableCity0.setImage(availableCities.isEmpty() ? null : availableCities.get(0));

    availableCity1.setImage(availableCities.size() > 1 ? availableCities.get(1) : null);

    availableCity2.setImage(availableCities.size() > 2 ? availableCities.get(2) : null);
  }
}
