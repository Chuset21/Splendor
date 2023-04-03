package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.ServerCaller;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.PurchaseCardForm;
import com.hexanome.fourteen.form.server.TakeGemsForm;
import com.hexanome.fourteen.form.server.payment.GemPaymentForm;
import com.hexanome.fourteen.form.server.tradingposts.TradingPostTakeGem;
import com.hexanome.fourteen.form.server.tradingposts.TradingPostsEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import kong.unirest.HttpResponse;

/**
 * A Bank Class to allow handling of Gems.
 */
public class Bank {

  // Fields [Internal]
  public static final int[] GEM_INDEX = {0, 1, 2, 3, 4, 5};
  public boolean isTaking; // If the Shop interface is open or not.
  private int[] bankGems = new int[6]; // bank's Gems (in Bank)
  private List<Integer> selectedGems; // gems the player is currently selecting
  private boolean selectingBonusGem; // if player is choosing bonus gem from post 3
  private PurchaseCardForm bonusGemCardForm;

  // Fields [Scene Nodes]
  List<Button> addGemButtons;
  List<Button> removeGemButtons;
  List<Label> gemLabels;
  // Includes reference to the players gems labels so bank can just update it itself.
  List<Label> bankGemLabels;
  Button openBankButton;
  Button takeBankButton;
  Pane takenTokenPane;
  GameBoard gameBoard;

  /**
   * Constructor for our Bank object to be made.
   *
   * @param numPlayers       The number of Players in the game
   * @param addGemButtons    The button to add Gems
   * @param removeGemButtons The button to remove Gems
   * @param gemLabels        The labels displaying the number of each Gem
   * @param bankGemLabels    The labels displaying the number of each Gem the Bank uses
   * @param openBankButton   Button to allow you open and close the Bank
   * @param takeBankButton   Button that allows you to take tokens from the bank
   */
  public Bank(int numPlayers,
              List<Button> addGemButtons,
              List<Button> removeGemButtons,
              List<Label> gemLabels,
              List<Label> bankGemLabels,
              Button openBankButton,
              Button takeBankButton,
              Pane takenTokenPane,
              GameBoard gameBoard) {

    // Initialize/Link our Objects
    this.addGemButtons = addGemButtons;
    this.removeGemButtons = removeGemButtons;
    this.gemLabels = gemLabels;
    this.bankGemLabels = bankGemLabels;
    this.openBankButton = openBankButton;
    this.takeBankButton = takeBankButton;
    this.selectedGems = new ArrayList<>();
    this.takenTokenPane = takenTokenPane;
    this.gameBoard = gameBoard;
    isTaking = false;
    selectingBonusGem = false;
    bonusGemCardForm = new PurchaseCardForm();

    // Initialize Starting Tokens and the respective Labels
    int initialTokens = (numPlayers < 4) ? (2 + numPlayers) : 7;

    for (int idx : GEM_INDEX) {
      bankGems[idx] = initialTokens;
      bankGemLabels.get(idx).textProperty().set("" + bankGems[idx]);
    }

    //Initialize Bank Button
    openBankButton.textProperty().set("Open");
    openBankButton.setPrefWidth(84);
  }

  public void updateGemCount(GemsForm bankGems) {
    if (!isTaking) {
      this.bankGems = GemsForm.costHashToArrayWithGold(bankGems);

      for (int i = 0; i < 6; i++) {
        bankGemLabels.get(i).textProperty().set("" + this.bankGems[i]);
      }
    }
  }

  /**
   * Toggles the Shop functionality which allows a Player to begin a buy session(Toggle on)
   * or hide the Shop(Toggle Off).
   */
  public void toggle() {
    isTaking = !isTaking;

    if (isTaking) {
      selectedGems.clear();
      openBankButton.textProperty().set("Cancel");
      openBankButton.setPrefWidth(100);
      takenTokenPane.setVisible(true);
      for (int idx : GEM_INDEX) {
        removeGemButtons.get(idx).setVisible(true);
        addGemButtons.get(idx).setVisible(true);
        if (idx < 5) {
          gemLabels.get(idx).setText("" + 0);
        }
      }
      if (!selectingBonusGem) {
        updateBankButtons();
      } else {
        updateButtonsForBonusGem();
      }

    } else {
      close(gameBoard.getGameBoardForm().availableGems());
    }
  }

  public void take() {
    final int totalGemsInHand = gameBoard.player.getHandForm().gems().count();

    // If taking gems would result in more than 10, go to tokenDiscarder
    if (selectedGems.size() + totalGemsInHand > 10) {
      openTokenDiscarder(selectedGems.size() + totalGemsInHand - 10);
    } else {
      // Send take gems action to the server
      sendTakeGems(selectedGems, null);
    }
  }

  /**
   * Send take gems action with gemsToRemove
   *
   * @param gemsToRemove
   */
  public void take(GemsForm gemsToRemove) {
    sendTakeGems(selectedGems, gemsToRemove);
  }

  public void takeBonusGem() {
    //check if there are too many gems
    final int totalGemsInHand = gameBoard.player.getHandForm().gems().count();
    int gemsToLoseFromPurchase = 0;
    if (bonusGemCardForm.payment() instanceof GemPaymentForm) {
      gemsToLoseFromPurchase =
          ((GemPaymentForm) bonusGemCardForm.payment()).getChosenGems().count();
    }
    if (totalGemsInHand - gemsToLoseFromPurchase + 1 > 10) {
      selectingBonusGem = false;
      openTokenDiscarder((totalGemsInHand - gemsToLoseFromPurchase + 1) - 10);
    } else {
      GemColor bonusGemColor = GemColor.INT_CONVERSION_ARRAY.get(selectedGems.get(0));
      TradingPostTakeGem tradingPostTakeGem = new TradingPostTakeGem(bonusGemColor, null);
      bonusGemCardForm.setTradingPostTakeGem(tradingPostTakeGem);
      selectingBonusGem = false;
      gameBoard.purchaseCard(bonusGemCardForm);
    }
  }

  public void takeBonusGem(GemsForm gemsToRemove) {
    GemColor bonusGemColor = GemColor.INT_CONVERSION_ARRAY.get(selectedGems.get(0));
    GemColor removeGemColor = GemColor.INT_CONVERSION_ARRAY.get(gemsToRemove.get(0));
    TradingPostTakeGem tradingPostTakeGem = new TradingPostTakeGem(bonusGemColor, removeGemColor);
    bonusGemCardForm.setTradingPostTakeGem(tradingPostTakeGem);
    selectingBonusGem = false;
    gameBoard.purchaseCard(bonusGemCardForm);
  }

  public void close(GemsForm gemsForm) {
    openBankButton.textProperty().set("Open");
    openBankButton.setPrefWidth(84);
    takenTokenPane.setVisible(false);

    // Otherwise, just hide all the buttons!
    for (int idx : GEM_INDEX) {
      removeGemButtons.get(idx).setVisible(false);
      addGemButtons.get(idx).setVisible(false);
    }

    isTaking = false;
    if (gemsForm != null) {
      updateGemCount(gemsForm);
    }
  }

  private void openTokenDiscarder(int tokensToDiscard) {
    final GemsForm gemsOwnedAfterTakingTokens = new GemsForm(gameBoard.player.getHandForm().gems());
    final GemsForm convertedForm = new GemsForm();

    selectedGems.forEach(i -> convertedForm.put(GemColor.INT_CONVERSION_ARRAY.get(i),
        convertedForm.getOrDefault(GemColor.INT_CONVERSION_ARRAY.get(i), 0) + 1));
    gemsOwnedAfterTakingTokens.addGems(convertedForm);
    if (selectingBonusGem) {
      gameBoard.tokenDiscarder =
          new TokenDiscarder(gameBoard, gemsOwnedAfterTakingTokens, tokensToDiscard,
              this::takeBonusGem);
    } else {
      gameBoard.tokenDiscarder =
          new TokenDiscarder(gameBoard, gemsOwnedAfterTakingTokens, tokensToDiscard, this::take);
    }
    gameBoard.tokenDiscarder.open();
  }

  public void sendTakeGems(List<Integer> takenGems, GemsForm gemsToRemove) {
    GemsForm convertedForm = new GemsForm();

    for (Integer i : takenGems) {
      convertedForm.put(GemColor.INT_CONVERSION_ARRAY.get(i),
          convertedForm.getOrDefault(GemColor.INT_CONVERSION_ARRAY.get(i), 0) + 1);
    }

    TakeGemsForm form = new TakeGemsForm(convertedForm, gemsToRemove);

    HttpResponse<String> response =
        ServerCaller.takeGems(LobbyServiceCaller.getCurrentUserLobby(),
            LobbyServiceCaller.getCurrentUserAccessToken(), form);

    gameBoard.closeAllActionWindows();
    gameBoard.updateBoard();
    gameBoard.acquireNobleAndCityCheck(response);
  }

  /**
   * Allows a Player to take Gems.
   *
   * @param index The index in the list
   */
  public void takeGem(int index) {

    // Update the internal values
    selectedGems.add(index);
    bankGems[index]--;

    // Update our text properties (Bank and Hand)
    bankGemLabels.get(index).textProperty().set("" + bankGems[index]);
    gemLabels.get(index).setText("" + (Integer.parseInt(gemLabels.get(index).getText()) + 1));

    // Update the buttons (specifically, their Enabled/Disabled state)
    if (!selectingBonusGem) {
      updateBankButtons();
    } else {
      updateButtonsForBonusGem();
    }
  }

  /**
   * Allows a Player to return Gems.
   *
   * @param index The index in the list
   */
  public void returnGem(int index) {

    //Update Values
    selectedGems.remove(Integer.valueOf(index));
    bankGems[index]++;

    // Update our text properties (Bank and Hand)
    bankGemLabels.get(index).textProperty().set("" + bankGems[index]);
    gemLabels.get(index).setText("" + (Integer.valueOf(gemLabels.get(index).getText()) - 1));

    // Update the buttons (specifically, their Abled/Disabled state)
    updateBankButtons();

  }

  // Updates the Disabled/Abled state of our Buy/Return Buttons,
  // based off the values of our internal variables.
  private void updateBankButtons() {

    // handBucket : <gem, count> of our current hand.
    Hashtable<Integer, Integer> handBucket = new Hashtable<>();
    for (int idx : GEM_INDEX) {
      handBucket.put(idx, 0);
    }
    for (int gem : selectedGems) {
      handBucket.put(gem, handBucket.get(gem) + 1);
    }

    boolean hasPost2 = gameBoard.player.getHandForm().tradingPosts()
        .getOrDefault(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, false);

    // Go through the buttons of each colour and enable/disable them accordingly.
    for (int idx : GEM_INDEX) {

      // If we have the gem colour in our hand, 'remove' should be enabled;
      // Otherwise, disable the 'remove' button.
      /*Gem is gold*/
      removeGemButtons.get(idx).setDisable(!selectedGems.contains(idx) || idx == 5);
      // If (following 5 conditions) hold, 'add' should be enabled.
      //Otherwise, disable the 'add' button.
      //have post 2 and two of the same color disable only that color
      addGemButtons.get(idx).setDisable(idx == 5 /*Gem is gold*/
                                        || bankGems[idx] == 0
                                        // there are no gems left of this color
                                        || (bankGems[idx] < 3 && selectedGems.contains(idx))
                                        // cant take 2 when there are less than 4
                                        || handBucket.get(idx) > 0 && selectedGems.size() > 1 //
                                        || selectedGems.size() == 3 //have 3 gems already
                                        || handBucket.containsValue(2) && !hasPost2
                                        // have 2 of one color and dont have trading post 2
                                        || hasPost2 && selectedGems.size() == 2
                                           && selectedGems.get(0) == idx
                                           && selectedGems.get(1) == idx);

      // If not enough gems are taken
      if (getNumGemTypesTakeable() >= 3
          && ((selectedGems.size() < 2) || (selectedGems.size() == 2 && !hasDoubleColour()))) {
        takeBankButton.setDisable(true);
      } else if (getNumGemTypesTakeable() == 2 && !(selectedGems.size() == 2)) {
        takeBankButton.setDisable(true);
      } else if (getNumGemTypesTakeable() == 1
                 && !((selectedGems.size() == 1 && !hasDoubleColour())
                      || (selectedGems.size() == 2 && hasDoubleColour()))) {
        takeBankButton.setDisable(true);
      } else {
        takeBankButton.setDisable(hasPost2
                                         && selectedGems.size() != 3
                                         && otherColorsAvailable(selectedGems.get(0)));
      }
    }
  }

  public void getBonusGem(PurchaseCardForm purchaseCardForm) {
    this.selectingBonusGem = true;
    this.bonusGemCardForm = purchaseCardForm;
    if (getNumGemTypesTakeable() == 0) {
      gameBoard.purchaseCard(purchaseCardForm);
    } else {
      takeBankButton.setDisable(true);
      updateButtonsForBonusGem();
      toggle();
    }
  }


  private void updateButtonsForBonusGem() {
    for (int idx : GEM_INDEX) {
      // If we have the gem colour in our hand, 'remove' should be enabled;
      // Otherwise, disable the 'remove' button.
      if (!selectedGems.contains(idx) || idx == 5 /*Gem is gold*/) {
        removeGemButtons.get(idx).setDisable(true);
      } else {
        removeGemButtons.get(idx).setDisable(false);
      }

      if (idx == 5 || selectedGems.size() == 1 || bankGems[idx] == 0) {
        addGemButtons.get(idx).setDisable(true);
      } else {
        addGemButtons.get(idx).setDisable(false);
      }

      if (selectedGems.size() == 1) {
        takeBankButton.setDisable(false);
      } else {
        takeBankButton.setDisable(true);
      }
    }
  }

  private boolean hasDoubleColour() {
    // handBucket : <gem, count> of our current hand.
    HashSet<Integer> handBucket = new HashSet<>();
    for (int gem : selectedGems) {

      if (handBucket.contains(gem)) {
        return true;
      }

      handBucket.add(gem);
    }
    return false;
  }

  private int getNumGemTypesTakeable() {
    int numTypes = 0;

    for (int i = 0; i < 5; i++) {
      numTypes += (bankGems[i] > 0 || selectedGems.contains(i)) ? 1 : 0;
    }

    return numTypes;
  }

  private boolean otherColorsAvailable(int color) {
    for (int idx : GEM_INDEX) {
      if (idx != color && idx != 5 && bankGems[idx] > 0) {
        return true;
      }
    }
    return false;
  }
}
