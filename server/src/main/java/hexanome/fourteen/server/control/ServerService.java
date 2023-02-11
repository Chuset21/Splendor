package hexanome.fourteen.server.control;

import hexanome.fourteen.server.control.form.LoginForm;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import kong.unirest.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Server Service.
 */
@Service
public class ServerService {

  private final LobbyServiceCaller lobbyService;
  private final String username;
  private final String password;
  private final Set<String> gameServiceNames;
  private final GsonInstance gsonInstance;
  /**
   * Access Token needed.
   */
  public String accessToken;
  private String refreshToken;

  /**
   * Constructor.
   *
   * @param gsonInstance common gson instance
   * @param lobbyService Lobby service
   * @param username     The Username
   * @param password     The Password
   */
  public ServerService(@Autowired GsonInstance gsonInstance,
                       @Autowired LobbyServiceCaller lobbyService,
                       @Value("${service.username}") String username,
                       @Value("${service.password}") String password) {
    this.gsonInstance = gsonInstance;
    this.lobbyService = lobbyService;
    this.username = username;
    this.password = password;
    gameServiceNames =
        Arrays.stream(GameServiceName.values()).map(Enum::name).collect(Collectors.toSet());
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
   * Login and get an access and refresh tokens.
   *
   * @return true if successful, false otherwise
   */
  private boolean login() {
    HttpResponse<String> response = lobbyService.login(username, password);

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
    HttpResponse<String> loginResponse = lobbyService.login("maex", password);

    String curAccessToken =
        gsonInstance.gson.fromJson(loginResponse.getBody(), LoginForm.class).accessToken();

    // We are logged in as maex
    lobbyService.createUser(username, password, curAccessToken);

    // We have now added our user and are going to log out maex
    lobbyService.logout(curAccessToken);
  }

  /**
   * Register all game services.
   *
   * @return true if successful, false otherwise
   */
  private boolean registerGameServices() {
    String gameServices = lobbyService.getGameServices();
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
    return lobbyService.registerGameService(gameServiceName, accessToken);
  }

  /**
   * Update the access token.
   * This should be called when a call to LS fails due to the access token being expired.
   *
   * @return true if successful, false otherwise
   */
  public boolean refreshToken() {
    HttpResponse<String> response = lobbyService.refreshToken(refreshToken);

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
      lobbyService.unregisterGameService(gameServiceName, accessToken);
    }
  }
}
