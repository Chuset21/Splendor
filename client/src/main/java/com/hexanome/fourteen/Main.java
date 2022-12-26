package com.hexanome.fourteen;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hexanome.fourteen.lobbyui.ScreenController;
import com.hexanome.fourteen.login.LoginScreenController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class that launches the application.
 * The user should enter the IP address of the lobby service as a command line argument.
 * If the user enters no command line arguments then it will be assumed that the lobby service is
 * running on localhost.
 */
public class Main extends Application {

  public static final Gson GSON =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();
  private static final String HTTP_STRING = "http://%s:%s/";
  private static final String LOBBY_SERVICE_PORT = "4242";
  private static final String DEFAULT_IP = "127.0.0.1";
  public static String lsLocation;

  private static void parseArgs(String[] args) {
    lsLocation = HTTP_STRING.formatted(args.length > 0 ? args[0] : DEFAULT_IP, LOBBY_SERVICE_PORT);
  }

  public static void main(String[] args) {
    parseArgs(args);
    Application.launch(Main.class, args);
  }

  /**
   * Run on startup of application. Sends user to login screen
   * @param stage active stage passed by application
   */
  @Override
  public void start(Stage stage) throws Exception {
    ScreenController loginScreen = new LoginScreenController();
    loginScreen.goTo(stage);
  }
}