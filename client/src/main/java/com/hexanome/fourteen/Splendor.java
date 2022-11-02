package com.hexanome.fourteen;

import com.hexanome.fourteen.boards.OrientExpansion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Splendor extends Application {

    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private static final String TITLE = "Splendor";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(TITLE + " Hexanome 14");

        HBox box = new HBox();

        final Label l = new Label("Hello, JavaFX %s, running on Java %s.".formatted(
                System.getProperty("javafx.version"),
                System.getProperty("java.version"))
        );

        Button button = new Button("Go to Expansion");
        box.getChildren().add(button);
        box.getChildren().add(l);

        button.setOnAction(actionEvent -> {
            try {
                OrientExpansion.startGame(primaryStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        final Scene scene = new Scene(box, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}