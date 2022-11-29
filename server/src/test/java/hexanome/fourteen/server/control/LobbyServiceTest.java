package hexanome.fourteen.server.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
public class LobbyServiceTest {
  static GsonInstance gsonInstance;

  static LobbyService lobbyService;

  @BeforeAll
  public static void setUp() {
    gsonInstance = new GsonInstance();
    lobbyService = new LobbyService(gsonInstance, "dummy location", "4243",
        "dummy address");
  }

  @Test
  public void login() {
  }

  @Test
  public void createUser() {
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