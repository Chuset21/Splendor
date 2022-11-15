package com.hexanome.fourteen.boards;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Bank {

    // Fields [Internal]
    public static final int[] GEM_INDEX = {0, 1, 2, 3, 4, 5};
    private int[] pGems = new int[6]; // player's Gems (in Hand)
    private int[] bGems = new int[6]; // bank's Gems (in Bank)
    private List<Integer> selectedGems; // gems the player is currently selecting
    private int numPlayers;
    private boolean isTaking; // If the Shop interface is open or not.

    // Fields [Scene Nodes]
    List<Button> addGemButtons;
    List<Button> removeGemButtons;
    List<Label> pGemLabels;
    List<Label> bGemLabels;
    Button takeBankButton;

    public Bank(int numPlayers,
                List<Button> addGemButtons,
                List<Button> removeGemButtons,
                List<Label> pGemLabels,
                List<Label> bGemLabels,
                Button takeBankButton) {

        // Initialize/Link our Objects
        this.addGemButtons = addGemButtons;
        this.removeGemButtons = removeGemButtons;
        this.pGemLabels = pGemLabels;
        this.bGemLabels = bGemLabels;
        this.numPlayers = numPlayers;
        this.takeBankButton = takeBankButton;
        this.selectedGems = new ArrayList<>();
        isTaking = false;

        // Initialize Starting Tokens and the respective Labels
        int initialTokens = (numPlayers < 4) ? (2 + numPlayers) : 7;

        for (int idx : GEM_INDEX) {
            bGems[idx] = initialTokens;
            pGemLabels.get(idx).textProperty().set("" + pGems[idx]);
            bGemLabels.get(idx).textProperty().set("" + bGems[idx]);
        }

        //Initialize Bank Button
        takeBankButton.textProperty().set("Open");
    }

    // Toggles the SHOP.
    // If Toggled on, it begins a new buying session.
    // If Toggled off, it simply hides all shop buttons.
    public void toggle() {
        isTaking = !isTaking;
        if (isTaking) {
            selectedGems.clear();
            takeBankButton.textProperty().set("Take");
            for (int idx : GEM_INDEX) {
                removeGemButtons.get(idx).setVisible(true);
                addGemButtons.get(idx).setVisible(true);
            }
            updateBankButtons();
        } else {
            takeBankButton.textProperty().set("Open");
            // Otherwise, just hide all the buttons!
            for (int idx : GEM_INDEX) {
                removeGemButtons.get(idx).setVisible(false);
                addGemButtons.get(idx).setVisible(false);
            }
        }
    }

    public void takeGem(int index) {

        // Update the internal values
        selectedGems.add(index);
        pGems[index]++;
        bGems[index]--;

        // Update our text properties (Bank and Hand)
        bGemLabels.get(index).textProperty().set("" + bGems[index]);
        pGemLabels.get(index).textProperty().set("" + pGems[index]);

        // Update the buttons (specifically, their Abled/Disabled state)
        updateBankButtons();
    }

    public void returnGem(int index) {

        //Update Values
        selectedGems.remove(Integer.valueOf(index));
        pGems[index]--;
        bGems[index]++;

        // Update our text properties (Bank and Hand)
        bGemLabels.get(index).textProperty().set("" + bGems[index]);
        pGemLabels.get(index).textProperty().set("" + pGems[index]);

        // Update the buttons (specifically, their Abled/Disabled state)
        updateBankButtons();

    }

    // Updates the Disabled/Abled state of our Buy/Return Buttons,
    // based off the values of our internal variables.
    private void updateBankButtons() {

        // handBucket : <gem, count> of our current hand.
        Hashtable<Integer, Integer> handBucket = new Hashtable<>();
        for (int idx : GEM_INDEX) handBucket.put(idx, 0);
        for (int gem : selectedGems) handBucket.put(gem, handBucket.get(gem) + 1);

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
            if (bGems[idx] == 0
                    || (bGems[idx] < 4 && selectedGems.contains(idx))
                    || handBucket.get(idx) > 0 && selectedGems.size() > 1
                    || selectedGems.size() == 3
                    || handBucket.containsValue(2)) {
                addGemButtons.get(idx).setDisable(true);
            } else {
                addGemButtons.get(idx).setDisable(false);
            }

        }
    }


}
