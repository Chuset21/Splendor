package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.payment.GemPaymentForm;
import com.hexanome.fourteen.form.server.tradingposts.TradingPostsEnum;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
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

  @FXML
  GridPane substituteGemGrid;
  private int[] chosenGems;
  private int[] cardCost;
  final GameBoard gameBoard;


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
    int missingGold = missingGems.count();

    // Removes missing gems from starting cost
    GemsForm tempCost =
        cardForm.cost().getDiscountedCost(gameBoard.player.getHandForm().gemDiscounts());
    tempCost.removeGems(missingGems);
    tempCost.computeIfAbsent(GemColor.GOLD,
        f -> missingGold > 0 ? missingGold : null);


    chosenGems = GemsForm.costHashToArrayWithGold(tempCost);
    cardCost = GemsForm.costHashToArrayWithGold(
        cardForm.cost().getDiscountedCost(gameBoard.player.getHandForm().gemDiscounts()));

    // Set submit payment function and enable finish button
    this.lookupButton(ButtonType.FINISH).setOnMouseClicked(
        e -> endFunction.accept(new GemPaymentForm(GemsForm.costArrayToHash(chosenGems), 0)));
    this.lookupButton(ButtonType.FINISH).setDisable(false);

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

    if (getNodeIndexInGrid(source)[1] == 0) {
      substituteGem(source);
    } else {
      useGem(source);
    }

    updateButtonsAndText();
  }

  /**
   * Remove selected gem from payment and substitute it with a gold gem
   *
   * @param source button selection was made from
   */
  public void substituteGem(Button source) {
    chosenGems[getNodeIndexInGrid(source)[0]]--;
    chosenGems[5]++;
  }

  /**
   * Add selected gem to payment and remove one gold gem
   *
   * @param source button selection was made from
   */
  public void useGem(Button source) {
    chosenGems[getNodeIndexInGrid(source)[0]]++;
    chosenGems[5]--;
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
      } else
        // Handle substituteGemButton (-)
        if (getNodeIndexInGrid(n)[1] == 0) {
          if(gameBoard.player.getHandForm().tradingPosts().getOrDefault(TradingPostsEnum.DOUBLE_GOLD_GEMS, false)){
            ((Button) n).setText("-2");
          } else {
            ((Button) n).setText("-");
          }

          // Set active if (amt of gold gems < gold gems in hand && amt of this gem > 0)
          n.setDisable(!(chosenGems[5] <
              gameBoard.player.getHandForm().gems().getOrDefault(GemColor.GOLD, 0).intValue() &&
              chosenGems[getNodeIndexInGrid(n)[0]] > 0));
        } else
          // Handle useGemButton (+)
          if (getNodeIndexInGrid(n)[1] == 3) {
            if(gameBoard.player.getHandForm().tradingPosts().getOrDefault(TradingPostsEnum.DOUBLE_GOLD_GEMS, false)){
              ((Button) n).setText("+2");
            } else {
              ((Button) n).setText("+");
            }


            // Set active if (amt of chosen gems in this color < cost of this card for this color
            // && amt of chosen gems in this color < this color gem in your hand)
            n.setDisable(
                !(chosenGems[getNodeIndexInGrid(n)[0]] < cardCost[getNodeIndexInGrid(n)[0]] &&
                    chosenGems[getNodeIndexInGrid(n)[0]] < gameBoard.player.getHandForm().gems()
                        .getOrDefault(GemColor.values()[getNodeIndexInGrid(n)[0]], 0).intValue()));
          }
    }
  }
}
