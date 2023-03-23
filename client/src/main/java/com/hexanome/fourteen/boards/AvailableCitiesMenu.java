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
  private final ToggleGroup availableCities = new ToggleGroup();

  @FXML
  ToggleButton availableCity0;

  @FXML
  ImageView availableCity0Image;

  @FXML
  ToggleButton availableCity1;

  @FXML
  ImageView availableCity1Image;

  @FXML
  ToggleButton availableCity2;

  @FXML
  ImageView availableCity2Image;



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

    // Make sure the images cover the full toggle button
    availableCity0.setPadding(Insets.EMPTY);
    availableCity1.setPadding(Insets.EMPTY);
    availableCity2.setPadding(Insets.EMPTY);

    // Set close function
    this.lookupButton(ButtonType.CLOSE).setOnMouseClicked(e -> toggleVisibility());

    // Update our available cities
    updateCities();
  }

  public boolean toggleVisibility() {
    this.setVisible(!isVisible());
    return isVisible();
  }

  public void updateCities() {

    ArrayList<City> availableCities = City.interpretCities(gameBoard.getGameBoardForm());

    availableCity0.setUserData(availableCities.get(0).cityForm);
    availableCity0Image.setImage(availableCities.get(0));

    availableCity1.setUserData(availableCities.get(1).cityForm);
    availableCity1Image.setImage(availableCities.get(1));

    availableCity2.setUserData(availableCities.get(2).cityForm);
    availableCity2Image.setImage(availableCities.get(2));


    System.out.println("BP");
  }


}
