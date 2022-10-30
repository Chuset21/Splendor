package com.hexanome.fourteen;

import com.hexanome.fourteen.boards.OrientExpansion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.Objects;

public class Splendor extends Application {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int MARGIN_OUTER = 10;
    private static final String TITLE = "Splendor";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        final FXMLLoader example = new FXMLLoader(Objects.requireNonNull(getClass().getResource("login.fxml")));
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
            OrientExpansion.startGame(primaryStage);
        });

        final Scene scene = new Scene(box, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}