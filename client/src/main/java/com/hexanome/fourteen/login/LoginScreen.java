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
        TextField tf1 = new TextField();
        PasswordField tf2 = new PasswordField();

        Button b = new Button("Log in");
        b.setOnAction(e -> System.out.println("You entered user ID: " + tf1.getText() + " and password: " + tf2.getText()));

        GridPane root = new GridPane();
        root.addRow(0, id, tf1);
        root.addRow(1, pw, tf2);
        root.addRow(2, b);

        Scene scene = new Scene(root, 800, 200);
        stage.setScene(scene);
        stage.setTitle("Text Field Example");
        stage.show();
    }
}
