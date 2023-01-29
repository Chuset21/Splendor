package hexanome.fourteen.server.control;

import java.util.Locale;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Lobby service.
 */
@Service
public class LobbyService implements LobbyServiceCaller {

  private final String lsLocation;
  private final String gameServiceLocation;
  private final GsonInstance gsonInstance;

  /**
   * Constructor.
   *
   * @param gsonInstance common gson instance
   * @param port         The Port
   * @param address      The IP Address
   * @param lsLocation   The LobbyService Location
   */
  public LobbyService(@Autowired GsonInstance gsonInstance,
                      @Value("${ls.location}") String lsLocation,
                      @Value("${server.port}") String port,
                      @Value("${service.address}") String address) {
    this.gsonInstance = gsonInstance;
    this.lsLocation = lsLocation;
    gameServiceLocation = "http://%s:%s/".formatted(address, port);
  }

  @Override
  public HttpResponse<String> login(String username, String password) {
    return Unirest.post("%soauth/token".formatted(lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("grant_type", "password").queryString("username", username)
        .queryString("password", password)
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();
  }

  @Override
  public void createUser(String username, String password, String accessToken) {
    Unirest.put("%sapi/users/%s".formatted(lsLocation, username))
        .header("Content-Type", "application/json")
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken)
        .body(gsonInstance.gson.toJson(new AccountCreationForm(username, password))).asEmpty();
  }

  @Override
  public void logout(String accessToken) {
    Unirest.delete("%soauth/active".formatted(lsLocation)).queryString("access_token", accessToken)
        .asEmpty();
  }

  @Override
  public String getGameServices() {
    return Unirest.get("%sapi/gameservices".formatted(lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=").asString()
        .getBody();

  }

  @Override
  public boolean registerGameService(String gameServiceName, String accessToken) {
    return Unirest.put("%sapi/gameservices/%s".formatted(lsLocation, gameServiceName))
               .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
               .header("Content-Type", "application/json").queryString("access_token", accessToken)
               .body(gsonInstance.gson.toJson(
                   new RegisterGameServiceForm(gameServiceLocation, gameServiceName,
                       gameServiceName))).asEmpty().getStatus() == 200;
  }

  @Override
  public HttpResponse<String> refreshToken(String refreshToken) {
    return Unirest.post("%soauth/token".formatted(lsLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("grant_type", "refresh_token").queryString("refresh_token", refreshToken)
        .body("user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true")
        .asString();
  }

  @Override
  public void unregisterGameService(String gameServiceName, String accessToken) {
    Unirest.delete("%sapi/gameservices/%s".formatted(lsLocation, gameServiceName))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).asEmpty();
  }

  @Override
  public String getUsername(String accessToken) {
    return Unirest.post("%soauth/username".formatted(lsLocation))
        .queryString("access_token", accessToken).asString().getBody();
  }

  @Override
  public boolean isAdmin(String accessToken) {
    return Unirest.get("%soauth/role".formatted(lsLocation))
        .queryString("access_token", accessToken).asString()
        .mapBody(b -> b != null && b.toLowerCase(
            Locale.ROOT).contains("admin"));
  }
}
