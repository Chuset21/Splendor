package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.Splendor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class OrientExpansion {

    private static Scene aScene;
    //@FXML private Button takeFromBankButton;

    public static void startGame(Stage aPrimaryStage) {
        // Get path to fxml resource
        URL fxmlURL = Splendor.class.getResource("/com/hexanome/fourteen/OrientExpansionBoard.fxml");
        System.out.println("URL from FXML file:\n" + fxmlURL);

        // Set up fxml file loader
        FXMLLoader loader = new FXMLLoader(fxmlURL);

        // Import root from fxml file
        Parent root;
        try{
            root = loader.load();
        }catch(Exception e){
            System.out.println("Exception throw when loading fxml board file: "+e);
            return;
        }

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
