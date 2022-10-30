package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.Splendor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OrientExpansion {

    private static Scene aScene;

    public static void startGame(Stage aPrimaryStage) {
        Label l = new Label("Welcome to the game of Splendor");

        //Scene scene = new Scene(new StackPane(l), 750, 500);

        FXMLLoader loader = new FXMLLoader(Splendor.class.getResource("OrientExpansionBoard.fxml"));

        Parent root;
        try{
            root = loader.load();
        }catch(Exception e){
            System.out.println("Exception throw when loading fxml board file: "+e);
            return;
        }

        Scene scene = new Scene(root);

        aPrimaryStage.setScene(scene);
        aPrimaryStage.setResizable(false);
        aPrimaryStage.show();
    }


}
