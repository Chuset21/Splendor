package hexanome.fourteen.server.control;

import hexanome.fourteen.server.control.form.RegisterGameServiceForm;
import hexanome.fourteen.server.control.form.SaveGameForm;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
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
   * @param lsLocation   The LobbyService Location
   * @param serverName   The server name (null) if not running in prod
   * @throws SocketException SocketException is thrown if there
   *                         is an error getting the address of this device
   */
  public LobbyService(@Autowired GsonInstance gsonInstance,
                      @Value("${ls.location}") String lsLocation,
                      @Value("${server.port}") String port,
                      @Value("${server.location.name:#{null}}") String serverName)
      throws SocketException {
    this.gsonInstance = gsonInstance;
    this.lsLocation = lsLocation;
    // Check if we're running in a container
    final String address =
        serverName == null || serverName.contains("null") ? getAddress().getHostAddress() :
            serverName;
    gameServiceLocation = "http://%s:%s/splendor".formatted(address, port);
  }

  private static Inet4Address getAddress() throws SocketException {
    return Collections.list(NetworkInterface.getNetworkInterfaces()).stream()
        .map(NetworkInterface::getInetAddresses)
        .flatMap(inetAddresses -> Collections.list(inetAddresses).stream()).filter(
            inetAddress -> inetAddress instanceof Inet4Address
                           && !inetAddress.isLoopbackAddress()
                           && !inetAddress.getHostAddress().startsWith("127"))
        .map(inetAddress -> (Inet4Address) inetAddress).findFirst().orElseThrow();
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
               .body(
                   gsonInstance.gson.toJson(
                       new RegisterGameServiceForm(gameServiceLocation, gameServiceName,
                           gameServiceName)))
               .asEmpty().getStatus() == 200;
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
    final HttpResponse<String> result = Unirest.get("%soauth/username".formatted(lsLocation))
        .queryString("access_token", accessToken).asString();
    return result.getStatus() == HttpStatus.OK ? result.getBody() : null;
  }

  @Override
  public void saveGame(String accessToken, SaveGameForm saveGameForm) {
    Unirest.put("%sapi/gameservices/%s/savegames/%s".formatted(lsLocation, saveGameForm.gameName(),
            saveGameForm.saveGameid())).queryString("access_token", accessToken)
        .header("Content-Type", "application/json").body(gsonInstance.gson.toJson(saveGameForm))
        .asString();
  }
}
