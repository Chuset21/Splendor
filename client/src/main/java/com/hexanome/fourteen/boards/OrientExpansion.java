package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.Splendor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class OrientExpansion {

    private static Scene aScene;
    //@FXML private Button takeFromBankButton;

    public static void startGame(Stage aPrimaryStage) throws IOException {
        // Import root from fxml file
        Parent root = FXMLLoader.load(Objects.requireNonNull(OrientExpansion.class.getResource("OrientExpansionBoard.fxml")));

        // Set up root on stage (window) and initialize stage settings
        Scene scene = new Scene(root);

        aPrimaryStage.setScene(scene);
        aPrimaryStage.setTitle("Splendor");
        aPrimaryStage.setResizable(false);
        aPrimaryStage.centerOnScreen();
        aPrimaryStage.show();
    }

    /*@FXML
    private void HandleTakeFromBankButtonClick(){

    }*/

}
