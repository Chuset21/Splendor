package hexanome.fourteen.server.control;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hexanome.fourteen.server.model.LoginResponse;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Server Controller stub.
 */
@RestController
public class ServerController {

  public static final String LS_LOCATION = "http://127.0.0.1:4242/";
  private static final String USERNAME = "splendor";
  private static final String PASSWORD = "abc123_ABC123";
  private static final String GAME_SERVICE_NAME = "Splendor";
  private static final String GAME_SERVICE_LOCATION = "http://127.0.0.1:4243/splendor";
  private final Gson gson =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();
  public String accessToken;
  private String refreshToken;


  /**
   * Login and register when booting up the application.
   */
  @PostConstruct
  private void loginAndRegister() {
    if (!login()) {
      throw new RuntimeException("Was not able to login");
    }
    if (!registerGameService()) {
      throw new RuntimeException("Was not able to register with game service");
    }
  }

  /**
   * Login and get a access and refresh tokens.
   *
   * @return true if successful, false otherwise
   */
  private boolean login() {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=");

    String body =
        "user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true";

    HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
    try {
      ResponseEntity<String> responseEntity = rest.exchange(
          "%soauth/token?grant_type=password&username=%s&password=%s".formatted(LS_LOCATION,
              USERNAME,
              PASSWORD), HttpMethod.POST, requestEntity, String.class);
      if (responseEntity.getStatusCode().value() != 200) {
        return false;
      }

      String response = responseEntity.getBody();
      LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
      accessToken = encodePlusSign(loginResponse.accessToken());
      refreshToken = encodePlusSign(loginResponse.refreshToken());
      return true;
    } catch (HttpClientErrorException ignored) {
      return false;
    }
  }

  /**
   * Register the game service if not registered already.
   *
   * @return true if successful, false otherwise
   */
  private boolean registerGameService() {
    if (getGameService()) {
      return true;
    }

    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    headers.add("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=");

    String body = """
        {
            "location": "%s",
            "maxSessionPlayers": 4,
            "minSessionPlayers": 2,
            "name": "%s",
            "displayName": "%s.",
            "webSupport": "true"
        }""".formatted(GAME_SERVICE_LOCATION, GAME_SERVICE_NAME, GAME_SERVICE_NAME);

    HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
    try {
      ResponseEntity<String> responseEntity = rest.exchange(
          "%sapi/gameservices/%s?access_token=%s".formatted(LS_LOCATION, GAME_SERVICE_NAME,
              accessToken), HttpMethod.PUT, requestEntity, String.class);
      return responseEntity.getStatusCode().value() == 200;
    } catch (HttpClientErrorException ignored) {
      return false;
    }
  }

  /**
   * Check if the game service is already registered.
   *
   * @return true if the game service is already registered, false otherwise
   */
  private boolean getGameService() {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=");

    HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
    try {
      ResponseEntity<String> responseEntity =
          rest.exchange("%sapi/gameservices/%s".formatted(LS_LOCATION, GAME_SERVICE_NAME),
              HttpMethod.GET, requestEntity, String.class);
      return responseEntity.getStatusCode().value() == 200;
    } catch (HttpClientErrorException ignored) {
      return false;
    }
  }

  /**
   * Update the access token.
   * This should be called when a call to LS fails due to the access token being expired.
   *
   * @return true if successful, false otherwise
   */
  public boolean refreshToken() {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=");

    String body =
        "user_oauth_approval=true&_csrf=19beb2db-3807-4dd5-9f64-6c733462281b&authorize=true";

    HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
    try {
      ResponseEntity<String> responseEntity = rest.exchange(
          "%soauth/token?grant_type=refresh_token&refresh_token=%s".formatted(LS_LOCATION,
              refreshToken), HttpMethod.POST, requestEntity, String.class);
      if (responseEntity.getStatusCode().value() != 200) {
        return false;
      }

      String response = responseEntity.getBody();
      accessToken = encodePlusSign(gson.fromJson(response, LoginResponse.class).accessToken());
      return true;
    } catch (HttpClientErrorException ignored) {
      return false;
    }
  }


  /**
   * Unregister the game service before shutting down.
   */
  @PreDestroy
  private void unregisterGameService() {
    if (!tryUnregisterGameService()) {
      refreshToken();
      tryUnregisterGameService();
    }
  }

  /**
   * Try to unregister the game service.
   *
   * @return true if successful, false otherwise.
   */
  private boolean tryUnregisterGameService() {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=");

    HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
    try {
      ResponseEntity<String> responseEntity = rest.exchange(
          "%sapi/gameservices/%s?access_token=%s".formatted(LS_LOCATION, GAME_SERVICE_NAME,
              accessToken), HttpMethod.DELETE, requestEntity, String.class);
      return responseEntity.getStatusCode().value() == 200;
    } catch (HttpClientErrorException ignored) {
      return false;
    }
  }

  /**
   * Replace all plus signs with %2B for proper encoding in URL.
   *
   * @param rawString The string to be encoded
   * @return The encoded string.
   */
  private String encodePlusSign(String rawString) {
    return rawString.replace("+", "%2B");
  }
}
