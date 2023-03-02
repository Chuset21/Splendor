package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.ServerCaller;
import com.hexanome.fourteen.TokenRefreshFailedException;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.TakeGemsForm;
import com.hexanome.fourteen.lobbyui.MenuController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * A Bank Class to allow handling of Gems.
 */
public class Bank {

  // Fields [Internal]
  public static final int[] GEM_INDEX = {0, 1, 2, 3, 4, 5};
  public boolean isTaking; // If the Shop interface is open or not.
  private int[] bankGems = new int[6]; // bank's Gems (in Bank)
  private List<Integer> selectedGems; // gems the player is currently selecting

  // Fields [Scene Nodes]
  List<Button> addGemButtons;
  List<Button> removeGemButtons;
  List<Label> gemLabels;
  // Includes reference to the players gems labels so bank can just update it itself.
  List<Label> bankGemLabels;
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
   * @param takeBankButton   Button to allow you take Gems from the Bank
   */
  public Bank(int numPlayers,
              List<Button> addGemButtons,
              List<Button> removeGemButtons,
              List<Label> gemLabels,
              List<Label> bankGemLabels,
              Button takeBankButton,
              Pane takenTokenPane,
              GameBoard gameBoard) {

    // Initialize/Link our Objects
    this.addGemButtons = addGemButtons;
    this.removeGemButtons = removeGemButtons;
    this.gemLabels = gemLabels;
    this.bankGemLabels = bankGemLabels;
    this.takeBankButton = takeBankButton;
    this.selectedGems = new ArrayList<>();
    this.takenTokenPane = takenTokenPane;
    this.gameBoard = gameBoard;
    isTaking = false;

    // Initialize Starting Tokens and the respective Labels
    int initialTokens = (numPlayers < 4) ? (2 + numPlayers) : 7;

    for (int idx : GEM_INDEX) {
      bankGems[idx] = initialTokens;
      bankGemLabels.get(idx).textProperty().set("" + bankGems[idx]);
    }

    //Initialize Bank Button
    takeBankButton.textProperty().set("Open");
  }

  public void updateGemCount(GemsForm bankGems){
    if(!isTaking){
      this.bankGems = GemsForm.costHashToArrayWithGold(bankGems);

      for(int i = 0;i<6;i++){
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
      takeBankButton.textProperty().set("Take");
      takenTokenPane.setVisible(true);
      for (int idx : GEM_INDEX) {
        removeGemButtons.get(idx).setVisible(true);
        addGemButtons.get(idx).setVisible(true);
        if(idx < 5){
          gemLabels.get(idx).setText(""+0);
        }
      }
      updateBankButtons();

    } else {
      takeBankButton.textProperty().set("Open");
      takenTokenPane.setVisible(false);
      // Otherwise, just hide all the buttons!
      for (int idx : GEM_INDEX) {
        removeGemButtons.get(idx).setVisible(false);
        addGemButtons.get(idx).setVisible(false);
      }

      // Send take gems action to the server
      sendTakeGems(selectedGems);
    }
  }

  public void sendTakeGems(List<Integer> takenGems){
    GemsForm convertedForm = new GemsForm();

    for(int i = 0; i<5;i++){
      convertedForm.put(GemColor.INT_CONVERSION_ARRAY.get(i), 0);
    }

    for(Integer i : takenGems){
      convertedForm.put(GemColor.INT_CONVERSION_ARRAY.get(i),convertedForm.get(GemColor.INT_CONVERSION_ARRAY.get(i)).intValue() + 1);
    }

    TakeGemsForm form = new TakeGemsForm(convertedForm, null);

    try {
      ServerCaller.takeGems(LobbyServiceCaller.getCurrentUserLobby(),
          LobbyServiceCaller.getCurrentUserAccessToken(), form);

      gameBoard.updateBoard();
    } catch (TokenRefreshFailedException e){
      try{
        MenuController.returnToLogin("Session timed out, retry login");
      } catch(IOException ioe){
        ioe.printStackTrace();
      }
    }
  }

  /**
   * Allows a Player to take Gems.
   *
   * @param index      The index in the list
   */
  public void takeGem(int index) {

    // Update the internal values
    selectedGems.add(index);
    bankGems[index]--;

    // Update our text properties (Bank and Hand)
    bankGemLabels.get(index).textProperty().set("" + bankGems[index]);
    gemLabels.get(index).setText(""+(Integer.valueOf(gemLabels.get(index).getText())+1));

    // Update the buttons (specifically, their Enabled/Disabled state)
    updateBankButtons();
  }

  /**
   * Allows a Player to return Gems.
   *
   * @param index      The index in the list
   */
  public void returnGem(int index) {

    //Update Values
    selectedGems.remove(Integer.valueOf(index));
    bankGems[index]++;

    // Update our text properties (Bank and Hand)
    bankGemLabels.get(index).textProperty().set("" + bankGems[index]);
    gemLabels.get(index).setText(""+(Integer.valueOf(gemLabels.get(index).getText())-1));

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

    // Go through the buttons of each colour and enable/disable them accordingly.
    for (int idx : GEM_INDEX) {

      // If we have the gem colour in our hand, 'remove' should be enabled;
      // Otherwise, disable the 'remove' button.
      if (!selectedGems.contains(idx)) {
        removeGemButtons.get(idx).setDisable(true);
      } else {
        removeGemButtons.get(idx).setDisable(false);
      }
      // If (following 5 conditions) hold, 'add' should be enabled.
      //Otherwise, disable the 'add' button.
      if (bankGems[idx] == 0
          || (bankGems[idx] < 4 && selectedGems.contains(idx))
          || handBucket.get(idx) > 0 && selectedGems.size() > 1
          || selectedGems.size() == 3
          || handBucket.containsValue(2)) {
        addGemButtons.get(idx).setDisable(true);
      } else {
        addGemButtons.get(idx).setDisable(false);
      }

      // If not enough gems are taken
      if ((selectedGems.size() < 2) || (selectedGems.size() == 2 && !hasDoubleColour())) {
        takeBankButton.setDisable(true);
      } else {
        takeBankButton.setDisable(false);
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
}
