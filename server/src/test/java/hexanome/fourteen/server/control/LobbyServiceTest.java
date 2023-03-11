package hexanome.fourteen.server.control;

import java.net.SocketException;
import kong.unirest.HttpResponse;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.util.ReflectionTestUtils;

@TestInstance(PER_CLASS)
public class LobbyServiceTest {
  static GsonInstance gsonInstance;

  static LobbyService lobbyService;

  @BeforeAll
  public static void setUp() throws SocketException {
    gsonInstance = new GsonInstance();
    ReflectionTestUtils.invokeMethod(gsonInstance, "initGson");
    lobbyService = new LobbyService(gsonInstance, "dummy location", "4243", "sp");
  }

  @Test
  public void login() {
  }

  @Test
  public void createUser() {
    HttpResponse response = mock(HttpResponse.class);
    //lobbyService.createUser("user", "password", "token");
  }

  @Test
  public void logout() {
  }

  @Test
  public void getGameServices() {
  }

  @Test
  public void registerGameService() {
  }

  @Test
  public void refreshToken() {
  }

  @Test
  public void unregisterGameService() {
  }

  @Test
  public void getUsername() {
  }
}