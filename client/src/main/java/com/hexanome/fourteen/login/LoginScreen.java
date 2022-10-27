package com.hexanome.fourteen.login;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;

public class LoginScreen extends Application {

    private static final String SPLENDOR_TEXT = "SPLENDOR";
    private static final String PASSWORD_PROMPT = "Password";
    private static final String USERNAME_PROMPT = "Username";

    @Override
    public void start(Stage stage) {
        Button splendorText = new Button(SPLENDOR_TEXT);
        splendorText.getStyleClass().add("splendor-title");

        TextField username = new TextField();
        username.setPromptText(USERNAME_PROMPT);

        PasswordField password = new PasswordField();
        password.setPromptText(PASSWORD_PROMPT);

        Button loginButton = createButton(true);
        loginButton.setOnAction(e -> System.out.printf("You entered user ID: %s and password: %s%n", username.getText(), password.getText()));

        Button quitButton = createButton(false);

        GridPane grid = new GridPane();
        grid.add(splendorText, 1, 0);
        grid.add(quitButton, 2, 0);
        grid.add(username, 1, 1);
        grid.add(password, 1, 2);
        grid.add(loginButton, 1, 3);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add("login.css");

        stage.setScene(scene);
        stage.setTitle("Login Screen");
        stage.setMinHeight(540);
        stage.setMinWidth(960);
        stage.setFullScreen(true);
        stage.show();
    }

    private Button createButton(boolean isLogIn) {
        final Button button = new Button(isLogIn ? "Log In" : "Quit");
        button.getStyleClass().add(isLogIn ? "login" : "quit");

        return button;
    }
}
