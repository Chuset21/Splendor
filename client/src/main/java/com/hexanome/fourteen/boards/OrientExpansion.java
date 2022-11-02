package com.hexanome.fourteen.boards;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrientExpansion {

    private static Scene aScene;
    @FXML private Button takeBankButton;
    @FXML private Pane menuPopupPane;

    @FXML private List<Label> pGemLabels;
    @FXML private List<Label> bGemLabels;
    @FXML private List<Button> removeGemButtons;
    @FXML private List<Button> addGemButtons;


    private int[] pGems = {2, 1, 2, 1, 0, 1};
    private int[] bGems = {3, 3, 7, 3, 3, 3};

    private int gemsTaken = 0;
    private boolean isTaking = false;
    private List<Integer> gemsSelected;

    public void startGame(Stage aPrimaryStage) throws IOException {
        // Import root from fxml file
        Parent root = FXMLLoader.load(Objects.requireNonNull(OrientExpansion.class.getResource("OrientExpansionBoard1600x900.fxml")));

        // Set up root on stage (window)
        Scene aScene = new Scene(root,1440,810);

        // Initialize stage settings
        aPrimaryStage.setScene(aScene);
        aPrimaryStage.setTitle("Splendor");
        aPrimaryStage.setResizable(false);
        aPrimaryStage.centerOnScreen();
        aPrimaryStage.show();
    }




    public void handleTakeGreenGemButton() {
        takeGem(0);
    }

    public void handleReturnGreenGemButton() {
        returnGem(0);
    }
    public void handleTakeWhiteGemButton() {
        takeGem(1);
    }

    public void handleReturnWhiteGemButton() {
        returnGem(1);
    }

    public void handleTakeBlueGemButton() {
        takeGem(2);
    }

    public void handleReturnBlueGemButton() {
        returnGem(2);
    }

    public void handleTakeBlackGemButton() {
        takeGem(3);
    }

    public void handleReturnBlackGemButton() {
        returnGem(3);
    }

    public void handleTakeRedGemButton() {
        takeGem(4);
    }

    public void handleReturnRedGemButton() {
        returnGem(4);
    }

    public void handleTakeYellowGemButton() {
        takeGem(5);
    }

    public void handleReturnYellowGemButton() {
        returnGem(5);
    }




    @FXML
    private void handleClickTakeBankButton(){
        System.out.println(takeBankButton + " has been pressed");
        isTaking = !isTaking;

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

    @FXML
    private void handleClickMenuButton(){
        menuPopupPane.setVisible(true);
    }

    @FXML
    private void handleClickMenuPopupBackButton(){
        menuPopupPane.setVisible(false);
    }

    @FXML
    private void handleClickMenuPopupQuitButton(){
        Platform.exit();
        System.exit(0);
    }

    public void takeGem(int index) {
        if (gemsTaken <= 3) {

            gemsTaken++;
            gemsSelected.add(index);
            pGems[index]++;
            bGems[index]--;

            bGemLabels.get(index).textProperty().set(""+bGems[index]);
            pGemLabels.get(index).textProperty().set(""+pGems[index]);

            removeGemButtons.get(index).setDisable(false);
            // If we hit 0 from this subtraction, disable the add button
            if (bGems[index] == 0) {
                addGemButtons.get(index).setDisable(true);
            }

            // If we took our maximum amount of gems, disable all buttons except "remove" for the gems we have
            if (gemsTaken == 3) {
                for (int i = 0 ; i < 6; i++) {
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

    public void returnGem(int index) {
        if (gemsTaken > 0) {

            gemsTaken--;
            gemsSelected.remove(Integer.valueOf(index));
            pGems[index]--;
            bGems[index]++;

            bGemLabels.get(index).textProperty().set(""+bGems[index]);
            pGemLabels.get(index).textProperty().set(""+pGems[index]);

            // If we went from 0 to 1, reenable the "add" button
            if (bGems[index] == 1) {
                addGemButtons.get(index).setDisable(false);
            }

            if (!gemsSelected.contains(index)) {
                removeGemButtons.get(index).setDisable(true);
            }

            // If we went from max to 2
            if (gemsTaken == 2) {
                for (int i = 0 ; i < 6; i++) {

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


