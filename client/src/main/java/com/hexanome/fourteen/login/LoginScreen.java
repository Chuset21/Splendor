package com.hexanome.fourteen.login;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class LoginScreen extends Application {
    @Override
    public void start(Stage stage) {
        Label id = new Label("User ID");
        Label pw = new Label("Password");
        TextField tf = new TextField();
        PasswordField password = new PasswordField();

        Button loginButton = createButton(true);
        loginButton.setOnAction(e -> System.out.printf("You entered user ID: %s and password: %s%n", tf.getText(), password.getText()));

        final Button quit = createButton(false);

        GridPane root = new GridPane();
        root.addRow(0, id, tf);
        root.addRow(1, pw, password);
        root.addRow(2, loginButton);
        root.addRow(0, quit);

        Scene scene = new Scene(root, 800, 450);
        scene.getStylesheets().add("login.css");
        stage.setScene(scene);
        stage.setTitle("Text Field Example");
        stage.show();
    }

    private Button createButton(boolean isLogIn) {
        final Button button = new Button(isLogIn ? "Log In" : "Quit");
        button.getStyleClass().add(isLogIn ? "login" : "quit");

        return button;
    }
}
