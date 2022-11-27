package hexanome.fourteen.server.control;

import hexanome.fourteen.server.Mapper;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Server Service.
 */
@Service
public class ServerService {

  private final String username;
  private final String password;
  private final String lsLocation;
  private final String gameServiceLocation;
  private final String[] gameServiceNames;
  private final GsonInstance gsonInstance;
  public String accessToken;
  private String refreshToken;

  /**
   * Constructor.
   *
   * @param gsonInstance          common gson instance
   * @param expansionStringMapper expansion to string mapper
   */
  public ServerService(@Autowired GsonInstance gsonInstance,
                       @Autowired Mapper<Expansion, String> expansionStringMapper,
                       @Value("${server.port}") String port,
                       @Value("${service.address}") String address,
                       @Value("${ls.location}") String lsLocation,
                       @Value("${service.username}") String username,
                       @Value("${service.password}") String password) {
    this.gsonInstance = gsonInstance;
    gameServiceLocation = "http://%s:%s/".formatted(address, port);
    this.lsLocation = lsLocation;
    this.username = username;
    this.password = password;
    gameServiceNames = new String[] {expansionStringMapper.map(Expansion.STANDARD),
        expansionStringMapper.map(Expansion.ORIENT)};
  }

  /**
   * Login and register when booting up the application.
   */
  @PostConstruct
  private void loginAndRegister() {
    if (!login()) {
      // If login failed, create the user and try again
      createUser();
      if (!login()) {
        throw new RuntimeException("Was not able to login");
      }
    }
    if (!registerGameServices()) {
      throw new RuntimeException("Was not able to register the game services");
    }
  }

  /**
   * Login and get a access and refresh tokens.
   *
   * @return true if successful, false otherwise
   */
  private boolean login() {
    HttpResponse<String> response = Unirest.post("%soauth/token".formatted(lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("grant_type", "password").queryString("username", username)
        .queryString("password", password)
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();

    if (response.getStatus() != 200) {
      return false;
    }

    LoginForm loginForm = gsonInstance.gson.fromJson(response.getBody(), LoginForm.class);
    accessToken = loginForm.accessToken();
    refreshToken = loginForm.refreshToken();
    return true;
  }

  /**
   * Create our user.
   */
  private void createUser() {
    HttpResponse<String> loginResponse = Unirest.post("%soauth/token".formatted(lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc")
        .queryString("grant_type", "password").queryString("username", "maex")
        .queryString("password", password)
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();

    String curAccessToken =
        gsonInstance.gson.fromJson(loginResponse.getBody(), LoginForm.class).accessToken();

    // We are logged in as maex
    Unirest.put("%sapi/users/%s".formatted(lsLocation, username))
        .header("Content-Type", "application/json")
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", curAccessToken)
        .body(gsonInstance.gson.toJson(new AccountCreationForm(username, password))).asEmpty();

    // We have now added our user and are going to log out maex
    Unirest.delete("%soauth/active".formatted(lsLocation))
        .queryString("access_token", curAccessToken).asEmpty();
  }

  /**
   * Register all game services.
   *
   * @return true if successful, false otherwise
   */
  private boolean registerGameServices() {
    HttpResponse<String> response = Unirest.get("%sapi/gameservices".formatted(lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=").asString();

    String gameServices = response.getBody();
    if (gameServices == null) {
      return false;
    }

    for (String gameServiceName : gameServiceNames) {
      if (!gameServices.contains(gameServiceName)) {
        if (!registerGameService(gameServiceName)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Register the game service.
   *
   * @param gameServiceName the game service to be registered
   * @return true if successful, false otherwise
   */
  private boolean registerGameService(String gameServiceName) {
    return Unirest.put("%sapi/gameservices/%s".formatted(lsLocation, gameServiceName))
               .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
               .header("Content-Type", "application/json").queryString("access_token", accessToken)
               .body(gsonInstance.gson.toJson(
                   new RegisterGameServiceForm(gameServiceLocation, gameServiceName,
                       gameServiceName))).asEmpty().getStatus() == 200;
  }

  /**
   * Update the access token.
   * This should be called when a call to LS fails due to the access token being expired.
   *
   * @return true if successful, false otherwise
   */
  public boolean refreshToken() {
    HttpResponse<String> response = Unirest.post("%soauth/token".formatted(lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("grant_type", "refresh_token").queryString("refresh_token", refreshToken)
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();

    if (response.getStatus() != 200) {
      return false;
    }

    accessToken = gsonInstance.gson.fromJson(response.getBody(), LoginForm.class).accessToken();
    return true;
  }


  /**
   * Unregister the game service before shutting down.
   */
  @PreDestroy
  private void unregisterGameServices() {
    refreshToken();
    tryUnregisterGameServices();
  }

  /**
   * Unregister all game services.
   */
  private void tryUnregisterGameServices() {
    for (String gameServiceName : gameServiceNames) {
      unregisterGameService(gameServiceName);
    }
  }

  /**
   * Try to unregister the game service.
   *
   * @param gameServiceName the game service to be unregistered
   */
  private void unregisterGameService(String gameServiceName) {
    Unirest.delete("%sapi/gameservices/%s".formatted(lsLocation, gameServiceName))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).asEmpty();
  }
}
