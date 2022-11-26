package com.hexanome.fourteen;

import com.hexanome.fourteen.login.LoginScreen;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class that launches the application.
 * The user should enter the IP address of the lobby service and server as command line arguments,
 * in that order.
 * If the user only enters one IP then it will be assumed that the lobby service and server are
 * running on the same machine.
 * If the user enters no command line arguments then it will be assumed that the lobby service and
 * server are running on localhost.
 */
public class Main extends Application {

  private static final String HTTP_STRING = "http://%s:%s/";
  private static final String LOBBY_SERVICE_PORT = "4042";
  private static final String SERVER_PORT = "4043";
  private static final String DEFAULT_IP = "127.0.0.1";
  public static String lsLocation;
  public static String serverLocation;

  public static void main(String[] args) {
    parseArgs(args);
    Application.launch(Main.class, args);
  }

  private static void parseArgs(String[] args) {
    if (args.length > 0) {
      lsLocation = HTTP_STRING.formatted(args[0], LOBBY_SERVICE_PORT);
      serverLocation = HTTP_STRING.formatted(args[args.length > 1 ? 1 : 0], SERVER_PORT);
    } else {
      lsLocation = HTTP_STRING.formatted(DEFAULT_IP, LOBBY_SERVICE_PORT);
      serverLocation = HTTP_STRING.formatted(DEFAULT_IP, SERVER_PORT);
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    LoginScreen ls = new LoginScreen();
    ls.goToLogin(stage);
  }
}