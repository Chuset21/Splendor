package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.payment.GemPaymentForm;
import com.hexanome.fourteen.form.server.tradingposts.TradingPostsEnum;
import java.io.IOException;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GoldGemSubstituteMenu extends DialogPane {

  static final String ADD_GEMS = "Add %d more gem%s to payment:";
  static final String REMOVE_GEMS_VALID = "Payment is valid, but overpaying by %d gem%s!";
  static final String REMOVE_GEMS = "Overpaying by %d gem%s!";
  static final String ACCEPT_GEMS = "Payment is valid!";
  final GameBoard gameBoard;
  @FXML
  GridPane substituteGemGrid;
  @FXML
  GridPane chooseGoldGemGrid;
  @FXML
  GridPane chooseGoldCardGrid;
  @FXML
  Label instructionLabel;
  private int[] chosenGems;
  private int[] cardCost;
  private GemsForm costDifference;
  private int chosenGoldCards;


  public GoldGemSubstituteMenu(GameBoard gameBoard) throws IOException {
    super();
    // Load GoldGemSubstituteMenu.fxml
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
        GoldGemSubstituteMenu.class.getResource("GoldGemSubstituteMenu.fxml")));

    // Apply the UI to this class (set this object as the root)
    loader.setRoot(this);

    // THIS IS IMPORTANT, you can't set this object as the root unless the FXML file HAS NO CONTROLLER SET & it has fx:root="Pane"
    loader.setController(this);
    loader.load();

    this.gameBoard = gameBoard;

    // Disable finish button
    this.lookupButton(ButtonType.FINISH).setDisable(true);

    // Start menu as invisible
    this.setVisible(false);
  }

  /**
   * Makes menu visible and resets all menu data
   *
   * @param endFunction function to be called when substitution is done, takes a {@link GemPaymentForm} parameter representing the chosen gems
   */
  public void open(CardForm cardForm, Consumer<GemPaymentForm> endFunction) {
    // Resets chosenGems and originalGems to (normal cost - discounts)

    // Finds missing gems
    GemsForm missingGems =
        cardForm.cost().getDiscountedCost(gameBoard.player.getHandForm().gemDiscounts());
    missingGems.removeGems(gameBoard.player.getHandForm().gems());

    // Removes missing gems from starting cost
    GemsForm tempCost =
        cardForm.cost().getDiscountedCost(gameBoard.player.getHandForm().gemDiscounts());
    tempCost.removeGems(missingGems);

    // Sets the starting cost, chosen gems and chosen cards for payment
    chosenGems = GemsForm.costHashToArrayWithGold(tempCost);
    chosenGoldCards = 0;
    cardCost = GemsForm.costHashToArrayWithGold(
        cardForm.cost().getDiscountedCost(gameBoard.player.getHandForm().gemDiscounts()));

    // Calculate difference between the cost and the gems chosen
    costDifference = GemsForm.costArrayToHash(cardCost);
    costDifference.removeGems(GemsForm.costArrayToHash(chosenGems));


    // Set submit payment function and enable finish button
    this.lookupButton(ButtonType.FINISH).setOnMouseClicked(
        e -> endFunction.accept(
            new GemPaymentForm(GemsForm.costArrayToHash(chosenGems), chosenGoldCards)));
    this.lookupButton(ButtonType.FINISH).setDisable(false);

    // Hide all other menus
    gameBoard.closeAllActionWindows();

    // Set menu as visible
    updateButtonsAndText();
    this.toFront();
    this.setVisible(true);
  }

  /**
   * Makes menu invisible
   */
  public void close() {
    // Sets menu as invisible
    this.setVisible(false);
  }

  @FXML
  private void handleClick(MouseEvent e) {
    Button source = ((Button) e.getSource());

    if (source.getParent().equals(substituteGemGrid)) {
      if (getNodeIndexInGrid(source)[1] == 0) {
        chosenGems[getNodeIndexInGrid(source)[0]]--;
      } else {
        chosenGems[getNodeIndexInGrid(source)[0]]++;
      }
    } else if (source.getParent().equals(chooseGoldGemGrid)) {
      if (getNodeIndexInGrid(source)[1] == 0) {
        chosenGems[5]--;
      } else {
        chosenGems[5]++;
      }
    } else if (source.getParent().equals(chooseGoldCardGrid)) {
      if (getNodeIndexInGrid(source)[1] == 0) {
        chosenGoldCards--;
      } else {
        chosenGoldCards++;
      }
    }
    updateButtonsAndText();
  }

  /**
   * Returns index of node in a grid
   *
   * @param n
   * @return index as form {row, column};
   */
  private int[] getNodeIndexInGrid(Node n) {
    int row = (GridPane.getRowIndex(n) == null) ? 0 : GridPane.getRowIndex(n);
    int column = (GridPane.getColumnIndex(n) == null) ? 0 : GridPane.getColumnIndex(n);
    return new int[] {row, column};
  }

  private void updateButtonsAndText() {
    for (Node n : substituteGemGrid.getChildren()) {
      // Display all gem amounts
      if (getNodeIndexInGrid(n)[1] == 1) {
        ((Label) n).setText("" + chosenGems[getNodeIndexInGrid(n)[0]]);
      } else if (getNodeIndexInGrid(n)[1] == 0) {
        // Handle substituteGemButton (-)

        // Set active if (&&)
        //        - chosen(gem) > 0
        n.setDisable(!(chosenGems[getNodeIndexInGrid(n)[0]] > 0));
      } else if (getNodeIndexInGrid(n)[1] == 3) {
        // Handle useGemButton (+)

        // Set active if (&&)
        //        - 0 < cost(gem)
        //        - chosen(gem) < cost(gem)
        //        - chosen(gem) < hand(gem)
        n.setDisable(!(0 < cardCost[getNodeIndexInGrid(n)[0]] &&
            chosenGems[getNodeIndexInGrid(n)[0]] < cardCost[getNodeIndexInGrid(n)[0]] &&
            chosenGems[getNodeIndexInGrid(n)[0]] < gameBoard.player.getHandForm().gems()
                .getOrDefault(GemColor.values()[getNodeIndexInGrid(n)[0]], 0)));
      }
    }

    for (Node n : chooseGoldGemGrid.getChildren()) {
      if (getNodeIndexInGrid(n)[1] == 1) {
        ((Label) n).setText("" + chosenGems[5]);
      } else if (getNodeIndexInGrid(n)[1] == 0) {
        // Handle (-) button

        // Set active if (gold gems used > 0)
        n.setDisable(!(chosenGems[5] > 0));
      } else if (getNodeIndexInGrid(n)[1] == 3) {
        // Handle (+) button

        // Set active if (gold gems used < gold gems in hand)
        n.setDisable(!(chosenGems[5] <
            gameBoard.player.getHandForm().gems().getOrDefault(GemColor.GOLD, 0)));
      }
    }
    for (Node n : chooseGoldCardGrid.getChildren()) {
      if (getNodeIndexInGrid(n)[1] == 1) {
        ((Label) n).setText("" + chosenGoldCards);
      } else if (getNodeIndexInGrid(n)[1] == 0) {
        // Handle (-) button

        // Set active if (gold gem cards used > 0)
        n.setDisable(!(chosenGoldCards > 0));
      } else if (getNodeIndexInGrid(n)[1] == 3) {
        // Handle (+) button

        // Set active if (gold gem cards used < gold gem cards in hand)
        n.setDisable(!(chosenGoldCards < gameBoard.player.getHandForm().getNumGoldGemCards()));
      }
    }

    // Calculate difference between the cost and the gems chosen
    costDifference = GemsForm.costArrayToHash(cardCost);
    costDifference.removeGems(GemsForm.costArrayToHash(chosenGems));

    boolean isDoubleGemsEnabled = gameBoard.player.getHandForm().tradingPosts()
        .getOrDefault(TradingPostsEnum.DOUBLE_GOLD_GEMS, false);

    Stack<Integer> goldGems = new Stack<>();

    // Add gold gem cards selected to stack
    for (int i = 0; i < chosenGoldCards; i++) {
      goldGems.push(isDoubleGemsEnabled ? 2 : 1);
      goldGems.push(isDoubleGemsEnabled ? 2 : 1);
    }

    // Add gold tokens to stack
    for (int i = 0; i < chosenGems[5]; i++) {
      goldGems.push(isDoubleGemsEnabled ? 2 : 1);
    }

    // Check if payment is valid.
    boolean requireMoreGems = false;
    int surplusCount = 0;

    for (int price : GemsForm.costHashToArray(costDifference)) {
      while (price > 0) {
        if (goldGems.empty()) {
          requireMoreGems = true;
          break;
        }
        price -= goldGems.pop();
        if (price < 0) {
          surplusCount += Math.abs(price);
        }
      }
      if (requireMoreGems) {
        break;
      }
    }

    // Set the done button on or off
    this.lookupButton(ButtonType.FINISH).setDisable(requireMoreGems || !goldGems.empty());


    int goldDiscount = (chosenGoldCards * 2 + chosenGems[5]) * (isDoubleGemsEnabled ? 2 : 1);

    // Set the instructions text
    if (requireMoreGems) {
      instructionLabel.setText(ADD_GEMS.formatted((goldDiscount - costDifference.count()) * -1 + surplusCount,
          ((goldDiscount - costDifference.count()) * -1 > 1) ? "s" : ""));
    } else if (!goldGems.empty()) {
      instructionLabel.setText(REMOVE_GEMS.formatted(goldDiscount - costDifference.count(),
          ((goldDiscount - costDifference.count()) > 1) ? "s" : ""));
    } else {
      if (goldDiscount > costDifference.count()) {
        instructionLabel.setText(REMOVE_GEMS_VALID.formatted(goldDiscount - costDifference.count(),
            ((goldDiscount - costDifference.count()) > 1) ? "s" : ""));
      } else {
        instructionLabel.setText(ACCEPT_GEMS);
      }
    }
  }
}
