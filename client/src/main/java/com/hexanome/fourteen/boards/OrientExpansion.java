package com.hexanome.fourteen.boards;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OrientExpansion {

    private static Scene aScene;

    public static void startGame(Stage aPrimaryStage) {
        Label l = new Label("Welcome to the game of Splendor");

        Scene scene = new Scene(new StackPane(l), 750, 500);

        aPrimaryStage.setScene(scene);
        aPrimaryStage.show();
    }


}
