package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.form.server.GemsForm;
import java.util.Arrays;
import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class TokenDiscarder {

  private DialogPane discardTokenPane;
  private GridPane discardTokenGrid;
  private Label discardTokenLabel;
  private int tokensToRemove;
  private int[] returnedTokens;
  private GemsForm gemsOwnedAfterTaking;
  private final String TEXT_TEMPLATE = "Select %s gems to discard";

  public TokenDiscarder(GameBoard gb, GemsForm gemsOwnedAfterTaking, int tokensToRemove,
                        Consumer<GemsForm> returnMethod) {
    this.discardTokenPane = gb.discardTokenPane;
    this.discardTokenGrid = gb.discardTokenGrid;
    this.discardTokenLabel = gb.discardTokenLabel;
    this.tokensToRemove = tokensToRemove;
    this.gemsOwnedAfterTaking = gemsOwnedAfterTaking;

    returnedTokens = new int[] {0, 0, 0, 0, 0, 0};
    discardTokenLabel.setText(TEXT_TEMPLATE.formatted(tokensToRemove));
    updateButtonsAndText();
    discardTokenPane.setVisible(false);

    discardTokenPane.lookupButton(ButtonType.FINISH).setOnMouseClicked(e -> {
      if (getAmountTokensReturned() == 0) {
        System.out.println("Attempted to finish discardToken session early—not allowed");
        return;
      }

      returnMethod.accept(GemsForm.costArrayToHash(returnedTokens));

      close();
    });
  }

  public void open() {
    updateButtonsAndText();
    discardTokenPane.toFront();
    discardTokenPane.setVisible(true);
  }

  public void close() {
    discardTokenPane.setVisible(false);
  }

  public void handleClick(MouseEvent e) {
    Button source = ((Button) e.getSource());

    if (getNodeIndexInGrid(source)[1] == 0) {
      keepToken(source);
    } else {
      returnToken(source);
    }

    updateButtonsAndText();
  }

  public void keepToken(Button source) {
    returnedTokens[getNodeIndexInGrid(source)[0]]--;
  }

  public void returnToken(Button source) {
    returnedTokens[getNodeIndexInGrid(source)[0]]++;
  }

  private int getAmountTokensReturned() {
    return Arrays.stream(returnedTokens).sum();
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
    for (Node n : discardTokenGrid.getChildren()) {
      if (getNodeIndexInGrid(n)[1] == 1) {
        ((Label) n).setText("" + returnedTokens[getNodeIndexInGrid(n)[0]]);
      } else
        // Handle keepTokenButton (-)
        if (getNodeIndexInGrid(n)[1] == 0) {
          int numThisTokenReturned = returnedTokens[getNodeIndexInGrid(n)[0]];

          n.setDisable(numThisTokenReturned == 0);

        } else
          // Handle returnTokenButton (+)
          if (getNodeIndexInGrid(n)[1] == 3) {
            int numThisTokenReturned = returnedTokens[getNodeIndexInGrid(n)[0]];
            int numThisTokenInHand =
                GemsForm.costHashToArrayWithGold(gemsOwnedAfterTaking)[getNodeIndexInGrid(n)[0]];

            if (getAmountTokensReturned() < tokensToRemove
                && numThisTokenReturned < numThisTokenInHand) {
              n.setDisable(false);
            } else {
              n.setDisable(true);
            }
          }
    }

    discardTokenPane.lookupButton(ButtonType.FINISH)
        .setDisable(getAmountTokensReturned() != tokensToRemove);
  }
}
