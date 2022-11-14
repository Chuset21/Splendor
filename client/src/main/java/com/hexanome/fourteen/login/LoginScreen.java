package com.hexanome.fourteen.login;

import com.hexanome.fourteen.boards.OrientExpansion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public final class LoginScreen implements Initializable {

    private static Stage aPrimaryStage;

    private static final ArrayList<String[]> CREDENTIALS = new ArrayList<>();

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;

    public void goToLogin(Stage pStage) throws IOException {

        aPrimaryStage = pStage;

        // Import root from fxml file
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginScreen.class.getResource("LoginScreen.fxml")));

        // Set up root on stage (window)
        Scene aScene = new Scene(root);

        // Initialize stage settings
        aPrimaryStage.setScene(aScene);
        aPrimaryStage.setTitle("Splendor");
        aPrimaryStage.setResizable(false);
        aPrimaryStage.centerOnScreen();

        aPrimaryStage.show();
    }

    private void init(){
        // Initialize credentials list with set of logins
        CREDENTIALS.add(new String[]{"joebiden43","okay123"});
        CREDENTIALS.add(new String[]{"test","test"});
        CREDENTIALS.add(new String[]{"kai","hi"});
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
    }

    @FXML
    private void handleLogin(){
        // Check if input credentials match any in the list of credentials
        for(String[] cred : CREDENTIALS){
            if(usernameField.getText().equals(cred[0]) && passwordField.getText().equals(cred[1])){
                launchGame();
            }
        }

        // Pass failed login message
        loginButton.setText("Failed login\nTry again");

    }

    @FXML
    private void handleQuitButton(){
        Platform.exit();
        System.exit(0);
    }

    private void launchGame(){
        // Create new instance of a Splendor game
        OrientExpansion game = new OrientExpansion();

        // Try launching the game
        try{
            game.goToGame(aPrimaryStage);
        } catch(IOException ioe) {
            System.out.println("Error when starting game" + ioe);
        }
    }

}
