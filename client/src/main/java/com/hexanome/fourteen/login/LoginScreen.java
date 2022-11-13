package com.hexanome.fourteen.login;

import com.hexanome.fourteen.boards.OrientExpansion;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class LoginScreen extends Application {

    private static Stage aPrimaryStage;

    private static final String USERNAME_STUB = "joebiden43";
    private static final String PASSWORD_STUB = "okay123";

    private static final String SPLENDOR_TEXT = "SPLENDOR";
    private static final String PASSWORD_PROMPT = "Password";
    private static final String USERNAME_PROMPT = "Username";
    private static final String WRONG_CREDENTIALS_MSG = "Incorrect username or password entered";

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;

    @Override
    public void start(Stage stage) throws IOException {

        aPrimaryStage = stage;

        // Import root from fxml file
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginScreen.class.getResource("LoginScreen.fxml")));

        // Set up root on stage (window)
        Scene aScene = new Scene(root,1440,810);

        // Initialize stage settings
        aPrimaryStage.setScene(aScene);
        aPrimaryStage.setTitle("Splendor");
        aPrimaryStage.setResizable(false);
        aPrimaryStage.centerOnScreen();

        // Set up screen

        aPrimaryStage.show();

    }

    @FXML
    private void handleLogin(){
        if(usernameField.getText().equals(USERNAME_STUB) && passwordField.getText().equals( PASSWORD_STUB)){
            launchGame();
        } else{
            System.out.println("Username input = "+usernameField.getText()+"\nPassword input = "+passwordField.getText());
            loginButton.setText("Failed login\nTry again");
        }
    }

    private void launchGame(){
        OrientExpansion game = new OrientExpansion();

        try{
            game.startGame(aPrimaryStage);
        } catch(IOException ioe) {
            System.out.println("Error when starting game" + ioe);
        }
    }

}
