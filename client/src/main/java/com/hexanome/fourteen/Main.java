package com.hexanome.fourteen;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.cardform.DoubleBonusCardForm;
import com.hexanome.fourteen.form.server.cardform.GoldGemCardForm;
import com.hexanome.fourteen.form.server.cardform.ReserveNobleCardForm;
import com.hexanome.fourteen.form.server.cardform.SacrificeCardForm;
import com.hexanome.fourteen.form.server.cardform.SatchelCardForm;
import com.hexanome.fourteen.form.server.cardform.StandardCardForm;
import com.hexanome.fourteen.form.server.cardform.WaterfallCardForm;
import com.hexanome.fourteen.form.server.payment.CardPaymentForm;
import com.hexanome.fourteen.form.server.payment.GemPaymentForm;
import com.hexanome.fourteen.form.server.payment.PaymentForm;
import com.hexanome.fourteen.login.LoginScreen;
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
          .registerTypeAdapterFactory(
              RuntimeTypeAdapterFactory.of(CardForm.class).registerSubtype(GoldGemCardForm.class)
                  .registerSubtype(ReserveNobleCardForm.class)
                  .registerSubtype(SacrificeCardForm.class).registerSubtype(SatchelCardForm.class)
                  .registerSubtype(StandardCardForm.class).registerSubtype(WaterfallCardForm.class)
                  .registerSubtype(DoubleBonusCardForm.class)).registerTypeAdapterFactory(
              RuntimeTypeAdapterFactory.of(PaymentForm.class).registerSubtype(GemPaymentForm.class)
                  .registerSubtype(CardPaymentForm.class)).serializeNulls().create();
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