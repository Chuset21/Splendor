package com.hexanome.fourteen;

import com.hexanome.fourteen.login.LoginScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LoginScreen ls = new LoginScreen();
        ls.goToLogin(stage);
    }
}