package com.hexanome.fourteen.login;

import com.hexanome.fourteen.boards.OrientExpansion;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class LoginScreen extends Application {

    private static final String USERNAME_STUB = "joebiden43";
    private static final String PASSWORD_STUB = "okay123";

    private static final String SPLENDOR_TEXT = "SPLENDOR";
    private static final String PASSWORD_PROMPT = "Password";
    private static final String USERNAME_PROMPT = "Username";
    private static final String WRONG_CREDENTIALS_MSG = "Incorrect username or password entered";
    private static final double WIDTH = 1600;
    private static final double HEIGHT = 900;

    @Override
    public void start(Stage stage) {
        try{
            OrientExpansion.startGame(stage);
        } catch (Exception e){
            System.out.println("Error when starting game"+e);
        }
        final Button splendorText = new Button(SPLENDOR_TEXT);
        splendorText.getStyleClass().add("splendor-title");
        setSplendorTextPosition(splendorText);

        final TextField username = new TextField();
        username.setPromptText(USERNAME_PROMPT);
        setUsernamePosition(username);

        final PasswordField password = new PasswordField();
        password.setPromptText(PASSWORD_PROMPT);
        setPasswordPosition(password);

        username.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                password.requestFocus();
            }
        });

        // Have an invisible message, so it will only show when the wrong credentials are entered
        final Text wrongCredentialsMsg = new Text(WRONG_CREDENTIALS_MSG);
        wrongCredentialsMsg.setId("wrong-credentials");
        wrongCredentialsMsg.setFill(Color.TRANSPARENT);
        wrongCredentialsMsg.setTextAlignment(TextAlignment.CENTER);
        // Wrap it in a StackPane to center the message
        final StackPane centeredWCM = new StackPane(wrongCredentialsMsg);
        setCredentialsMsgPosition(centeredWCM);

        final Button loginButton = new Button("Log In");
        loginButton.setAlignment(Pos.CENTER);
        loginButton.getStyleClass().add("login");
        loginButton.setOnAction(e -> {
            final String usernameContents = username.getText();
            final String passwordContents = password.getText();
            if (!usernameContents.isBlank() && !passwordContents.isEmpty()) {
                if (usernameContents.equals(USERNAME_STUB) && passwordContents.equals(PASSWORD_STUB)) {
                    try {
                        OrientExpansion.startGame(stage);
                    } catch (IOException ioException) {
                        throw new RuntimeException(ioException);
                    }
                } else {
                    wrongCredentialsMsg.setFill(Color.RED);
                    password.clear();
                }
            }
        });
        setLoginButtonPosition(loginButton);

        password.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginButton.fire();
            }
        });

        final Button quitButton = new Button("Quit");
        quitButton.getStyleClass().add("quit");
        quitButton.setOnAction(e -> exitPlatform());
        setQuitButtonPosition(quitButton);

        final AnchorPane root = new AnchorPane(splendorText, quitButton, username, password, centeredWCM, loginButton);
        final Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("login.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Login Screen");
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> exitPlatform());
        stage.show();
    }

    private static void setQuitButtonPosition(Button quitButton) {
        AnchorPane.setTopAnchor(quitButton, 7d);
        AnchorPane.setRightAnchor(quitButton, 7d);
    }

    private static void setLoginButtonPosition(Button loginButton) {
        AnchorPane.setTopAnchor(loginButton, 560d);
        AnchorPane.setLeftAnchor(loginButton, 720d);
        AnchorPane.setRightAnchor(loginButton, 720d);
    }

    private static void setCredentialsMsgPosition(StackPane centeredWCM) {
        AnchorPane.setTopAnchor(centeredWCM, 490d);
        AnchorPane.setLeftAnchor(centeredWCM, 700d);
        AnchorPane.setRightAnchor(centeredWCM, 700d);
        AnchorPane.setBottomAnchor(centeredWCM, 360d);
    }

    private static void setPasswordPosition(PasswordField password) {
        AnchorPane.setTopAnchor(password, 420d);
        AnchorPane.setLeftAnchor(password, 700d);
        AnchorPane.setRightAnchor(password, 700d);
        AnchorPane.setBottomAnchor(password, 430d);
    }

    private static void setUsernamePosition(TextField username) {
        AnchorPane.setTopAnchor(username, 350d);
        AnchorPane.setLeftAnchor(username, 700d);
        AnchorPane.setRightAnchor(username, 700d);
        AnchorPane.setBottomAnchor(username, 500d);
    }

    private static void setSplendorTextPosition(Button splendorText) {
        AnchorPane.setTopAnchor(splendorText, 20d);
        AnchorPane.setLeftAnchor(splendorText, 400d);
        AnchorPane.setRightAnchor(splendorText, 400d);
    }

    /**
     * Shuts down the application
     */
    private static void exitPlatform() {
        Platform.exit();
        System.exit(0);
    }
}