package com.hexanome.fourteen.boards;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class OrientExpansion {

    private static Scene aScene;
    @FXML private Button takeBankButton;
    @FXML private Pane menuPopupPane;
    private static int[] pGems = {2, 1, 2, 1, 0, 1};
    private static int[] bGems = {2, 3, 7, 3, 3, 3};

    @FXML private Label pGreenGemLabel;
    @FXML private Label pWhiteGemLabel;
    @FXML private Label pBlueGemLabel;
    @FXML private Label pBlackGemLabel;
    @FXML private Label pRedGemLabel;
    @FXML private Label pYellowGemLabel;

    @FXML private Label bGreenGemLabel;
    @FXML private Label bWhiteGemLabel;
    @FXML private Label bBlueGemLabel;
    @FXML private Label bBlackGemLabel;
    @FXML private Label bRedGemLabel;
    @FXML private Label bYellowGemLabel;



    public static void startGame(Stage aPrimaryStage) throws IOException {
        // Import root from fxml file
        Parent root = FXMLLoader.load(Objects.requireNonNull(OrientExpansion.class.getResource("OrientExpansionBoard1600x900.fxml")));

        // Set up root on stage (window)
        Scene aScene = new Scene(root);

        // Initialize stage settings
        aPrimaryStage.setScene(aScene);
        aPrimaryStage.setTitle("Splendor");
        aPrimaryStage.setResizable(false);
        aPrimaryStage.centerOnScreen();
        aPrimaryStage.show();
    }

    @FXML
    private void handleClickTakeBankButton(){
        System.out.println(takeBankButton + " has been pressed");
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
}
