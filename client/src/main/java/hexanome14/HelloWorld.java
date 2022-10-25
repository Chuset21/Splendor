package hexanome14;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloWorld extends Application {

    @Override
    public void start(Stage stage) {
        final Label l = new Label("Hello, JavaFX %s, running on Java %s.".formatted(
                System.getProperty("javafx.version"),
                System.getProperty("java.version"))
        );
        final Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}