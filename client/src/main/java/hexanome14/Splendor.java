package hexanome14;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
            boards.OrientExpansion.startGame(primaryStage);
        });

        final Scene scene = new Scene(box, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}