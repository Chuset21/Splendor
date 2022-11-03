package com.hexanome.fourteen.boards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Class Description.
 */
public class OrientExpansion {

  private static Scene aScene;
  @FXML
  private Button takeBankButton;
  @FXML
  private Pane menuPopupPane;

  //CARD FIELDS
  //2D Array with all card FX ID names.
  @FXML
  private static final String[][] CARDS = new String[3][6];

  static {
    for (int row = 1; row < 4; row++) {
      for (int column = 1; column < 7; column++) {
        CARDS[row - 1][column - 1] = "R" + row + "C" + column;
      }
    }
  }

  @FXML
  private ImageView selectedCard;
  private String selectedCardId;
  @FXML
  private ImageView purchasedStack;
  @FXML
  private ImageView reservedStack;
  @FXML
  private Pane cardActionsView;

  //GEM FIELDS
  @FXML
  private List<Label> pGemLabels;
  @FXML
  private List<Label> bGemLabels;
  @FXML
  private List<Button> removeGemButtons;
  @FXML
  private List<Button> addGemButtons;
  private int[] pGems = {2, 1, 2, 1, 0, 1};
  private int[] bGems = {3, 3, 7, 3, 3, 3};
  private int gemsTaken = 0;
  private boolean isTaking = false;
  private List<Integer> gemsSelected;

  /**
   * Description.
   *
   * @param aPrimaryStage param description
   * @throws IOException if XYZ reason
   */
  public void startGame(Stage aPrimaryStage) throws IOException {
    // Import root from fxml file
    Parent root = FXMLLoader.load(Objects.requireNonNull(
        OrientExpansion.class.getResource("OrientExpansionBoard1600x900.fxml")));

    // Set up root on stage (window)
    Scene aScene = new Scene(root,1440,810);

    // Initialize stage settings
    aPrimaryStage.setScene(aScene);
    aPrimaryStage.setTitle("Splendor");
    aPrimaryStage.setResizable(false);
    aPrimaryStage.centerOnScreen();
    aPrimaryStage.show();
  }

  /**
   * Description.
   *
   * @param event if XYZ reason
   */
  public void handleCardSelect(MouseEvent event) {
    selectedCard = (ImageView) event.getSource();
    //Input validation for null spaces
    if (selectedCard.getImage() == null) {
      return;
    }
    selectedCardId = selectedCard.getId();
    cardActionsView.setVisible(true);
  }

  /**
   * Description.
   */
  public void handlePurchase() {
    Image cardPurchased = selectedCard.getImage();
    selectedCard.setImage(null);
    purchasedStack.setImage(cardPurchased);
    cardActionsView.setVisible(false);
  }

  /**
   * Description.
   */
  public void handleReserve() {
    Image cardReserved = selectedCard.getImage();
    selectedCard.setImage(null);
    reservedStack.setImage(cardReserved);
    cardActionsView.setVisible(false);
  }

  /**
   * Description.
   */
  public void handleTakeGreenGemButton() {
    takeGem(0);
  }

  /**
   * Description.
   */
  public void handleReturnGreenGemButton() {
    returnGem(0);
  }

  /**
   * Description.
   */
  public void handleTakeWhiteGemButton() {
    takeGem(1);
  }

  /**
   * Description.
   */
  public void handleReturnWhiteGemButton() {
    returnGem(1);
  }

  /**
   * Description.
   */
  public void handleTakeBlueGemButton() {
    takeGem(2);
  }

  /**
   * Description.
   */
  public void handleReturnBlueGemButton() {
    returnGem(2);
  }

  /**
   * Description.
   */
  public void handleTakeBlackGemButton() {
    takeGem(3);
  }

  /**
   * Description.
   */
  public void handleReturnBlackGemButton() {
    returnGem(3);
  }

  /**
   * Description.
   */
  public void handleTakeRedGemButton() {
    takeGem(4);
  }

  /**
   * Description.
   */
  public void handleReturnRedGemButton() {
    returnGem(4);
  }

  /**
   * Description.
   */
  public void handleTakeYellowGemButton() {
    takeGem(5);
  }

  /**
   * Description.
   */
  public void handleReturnYellowGemButton() {
    returnGem(5);
  }


  /**
   * Description.
   */
  @FXML
  private void handleClickTakeBankButton() {
    isTaking = !isTaking;

    if (isTaking) {
      takeBankButton.textProperty().set("Take");
    } else {
      takeBankButton.textProperty().set("Open");
    }

    if (isTaking) {
      gemsTaken = 0;
      gemsSelected = new ArrayList<Integer>();
      for (int i = 0; i < bGems.length; i++) {

        // Set remove buttons as visible but disabled
        removeGemButtons.get(i).setVisible(true);
        removeGemButtons.get(i).setDisable(true);

        // Set add buttons as visible, but disable if there's none in the bank
        addGemButtons.get(i).setVisible(true);
        if (bGems[i] == 0) {
          addGemButtons.get(i).setDisable(true);
        } else {
          addGemButtons.get(i).setDisable(false);
        }
      }
    } else {
      // Otherwise, just hide all the buttons!
      for (int i = 0; i < bGems.length; i++) {
        removeGemButtons.get(i).setVisible(false);
        addGemButtons.get(i).setVisible(false);
      }
    }
  }

  /**
   * Description.
   */
  @FXML
  private void handleClickMenuButton() {
    menuPopupPane.setVisible(true);
  }

  /**
   * Description.
   */
  @FXML
  private void handleClickMenuPopupBackButton() {
    menuPopupPane.setVisible(false);
  }

  /**
   * Description.
   */
  @FXML
  private void handleClickMenuPopupQuitButton() {
    Platform.exit();
    System.exit(0);
  }

  /**
   * Description.
   *
   * @param index if XYZ reason
   */
  public void takeGem(int index) {
    if (gemsTaken <= 3) {

      gemsTaken++;
      gemsSelected.add(index);
      pGems[index]++;
      bGems[index]--;

      bGemLabels.get(index).textProperty().set("" + bGems[index]);
      pGemLabels.get(index).textProperty().set("" + pGems[index]);

      removeGemButtons.get(index).setDisable(false);
      // If we hit 0 from this subtraction, disable the add button
      if (bGems[index] == 0) {
        addGemButtons.get(index).setDisable(true);
      }


      // If we took our maximum amount of gems
      // disable all buttons except "remove" for the gems we have
      if (gemsTaken == 3 || gemsSelected.indexOf(Integer.valueOf(index))
          != gemsSelected.lastIndexOf(Integer.valueOf(index))) {
        for (int i = 0; i < 6; i++) {
          addGemButtons.get(i).setDisable(true);
          if (gemsSelected.contains(i)) {
            removeGemButtons.get(i).setDisable(false);
          } else {
            removeGemButtons.get(i).setDisable(true);
          }
        }
      }
    }
  }

  /**
   * Description.
   *
   * @param index if XYZ reason
   */
  public void returnGem(int index) {
    if (gemsTaken > 0) {

      gemsTaken--;
      gemsSelected.remove(Integer.valueOf(index));
      pGems[index]--;
      bGems[index]++;

      bGemLabels.get(index).textProperty().set("" + bGems[index]);
      pGemLabels.get(index).textProperty().set("" + pGems[index]);

      // If we went from 0 to 1, re-enable the "add" button
      if (bGems[index] == 1) {
        addGemButtons.get(index).setDisable(false);
      }

      if (!gemsSelected.contains(index)) {
        removeGemButtons.get(index).setDisable(true);
      }


      // If we went from max to 2
      if (gemsTaken == 2 || (gemsTaken == 1 && gemsSelected.indexOf(Integer.valueOf(index))
          == gemsSelected.lastIndexOf(Integer.valueOf(index)))) {
        for (int i = 0; i < 6; i++) {

          if (gemsSelected.contains(i)) {
            removeGemButtons.get(i).setDisable(false);
          } else {
            removeGemButtons.get(i).setDisable(true);
          }

          // if we have inventory of that gem, enable the option
          if (bGems[i] > 0) {
            addGemButtons.get(i).setDisable(false);
          } else {
            addGemButtons.get(i).setDisable(true);
          }
        }
      }
    }
  }
}


