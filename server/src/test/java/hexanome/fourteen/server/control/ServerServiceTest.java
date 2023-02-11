package hexanome.fourteen.server.control;

import hexanome.fourteen.server.control.form.LoginForm;
import java.util.HashSet;
import java.util.Set;
import kong.unirest.HttpResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;

@TestInstance(PER_CLASS)
public class ServerServiceTest {

  LobbyServiceCaller lobbyService;
  static GsonInstance gsonInstance;
  final static String username = "test-username";
  final static String password = "test-password";
  ServerService serverService;

  @BeforeAll
  public static void setUp() {
    gsonInstance = new GsonInstance();
    ReflectionTestUtils.invokeMethod(gsonInstance, "initGson");
  }

  @BeforeEach
  public void init() {
    lobbyService = mock(LobbyServiceCaller.class);
    serverService =
        new ServerService(gsonInstance, lobbyService, username,
            password);
  }

  @Test
  public void testLogin() {
    HttpResponse response = mock(HttpResponse.class);
    Mockito.when(response.getBody()).thenReturn(null);
    Mockito.when(response.getStatus()).thenReturn(401);
    Mockito.when(lobbyService.login(username, password)).thenReturn(response);

    assertFalse((Boolean) ReflectionTestUtils.invokeMethod(serverService, "login"));

    Mockito.when(lobbyService.login(username, password)).thenReturn(response);
    Mockito.when(response.getBody())
        .thenReturn(gsonInstance.gson.toJson(new AccountCreationForm(username, password)));
    Mockito.when(response.getStatus()).thenReturn(200);

    assertTrue((Boolean) ReflectionTestUtils.invokeMethod(serverService, "login"));
  }

  @Test
  public void testRefreshToken() {
    HttpResponse response = mock(HttpResponse.class);
    Mockito.when(response.getBody()).thenReturn(null);
    Mockito.when(response.getStatus()).thenReturn(401);
    Mockito.when(lobbyService.refreshToken(null)).thenReturn(response);

    assertFalse((Boolean) ReflectionTestUtils.invokeMethod(serverService, "refreshToken"));

    Mockito.when(lobbyService.refreshToken("test")).thenReturn(response);
    final LoginForm x = new LoginForm();
    ReflectionTestUtils.setField(x, "accessToken", "test");
    ReflectionTestUtils.setField(x, "refreshToken", "test");
    Mockito.when(response.getBody()).thenReturn(gsonInstance.gson.toJson(x));
    Mockito.when(response.getStatus()).thenReturn(200);

    assertTrue((Boolean) ReflectionTestUtils.invokeMethod(serverService, "refreshToken"));
  }

  @Test
  public void testRegisterGameServices() {
    Mockito.when(lobbyService.getGameServices()).thenReturn(null);

    assertFalse((Boolean) ReflectionTestUtils.invokeMethod(serverService, "registerGameServices"));

    ReflectionTestUtils.setField(serverService, "gameServiceNames", new HashSet<>());
    Mockito.when(lobbyService.getGameServices()).thenReturn("xyz");

    assertTrue((Boolean) ReflectionTestUtils.invokeMethod(serverService, "registerGameServices"));

    ReflectionTestUtils.setField(serverService, "gameServiceNames", Set.of("x", "t"));
    Mockito.when(lobbyService.registerGameService("x", null)).thenReturn(true);
    Mockito.when(lobbyService.registerGameService("t", null)).thenReturn(false);

    assertFalse((Boolean) ReflectionTestUtils.invokeMethod(serverService, "registerGameServices"));
  }

  @Test
  public void testCreateUser() {
    HttpResponse response = mock(HttpResponse.class);
    Mockito.when(lobbyService.login("maex", password)).thenReturn(response);
    final LoginForm x = new LoginForm();
    ReflectionTestUtils.setField(x, "accessToken", "test");
    ReflectionTestUtils.setField(x, "refreshToken", "test");
    Mockito.when(response.getBody()).thenReturn(gsonInstance.gson.toJson(x, LoginForm.class));
    Mockito.when(response.getStatus()).thenReturn(200);
    ReflectionTestUtils.invokeMethod(serverService, "createUser");
  }

  @Test
  public void testLoginAndRegister() {
    HttpResponse response = mock(HttpResponse.class);
    Mockito.when(response.getBody()).thenReturn(null);
    Mockito.when(response.getStatus()).thenReturn(401);
    Mockito.when(lobbyService.login(username, password)).thenReturn(response);

    HttpResponse response2 = mock(HttpResponse.class);
    Mockito.when(lobbyService.login("maex", password)).thenReturn(response2);
    final LoginForm x = new LoginForm();
    ReflectionTestUtils.setField(x, "accessToken", "test");
    ReflectionTestUtils.setField(x, "refreshToken", "test");
    Mockito.when(response2.getBody()).thenReturn(gsonInstance.gson.toJson(x, LoginForm.class));
    Mockito.when(response2.getStatus()).thenReturn(200);

    try {
      ReflectionTestUtils.invokeMethod(serverService, "loginAndRegister");
    } catch (Exception e) {
      assertInstanceOf(RuntimeException.class, e);
      assertEquals("Was not able to login", e.getMessage());
    }

    Mockito.when(response.getBody()).thenReturn(gsonInstance.gson.toJson(x, LoginForm.class));
    Mockito.when(response.getStatus()).thenReturn(200);
    Mockito.when(lobbyService.getGameServices()).thenReturn(null);

    try {
      ReflectionTestUtils.invokeMethod(serverService, "loginAndRegister");
    } catch (Exception e) {
      assertInstanceOf(RuntimeException.class, e);
      assertEquals("Was not able to register the game services", e.getMessage());
    }
  }

  @Test
  public void testUnregisterGameServices() {
    HttpResponse response = mock(HttpResponse.class);
    Mockito.when(response.getBody()).thenReturn(null);
    Mockito.when(response.getStatus()).thenReturn(401);
    Mockito.when(lobbyService.refreshToken(null)).thenReturn(response);

    ReflectionTestUtils.setField(serverService, "gameServiceNames", Set.of("x", "t"));
    ReflectionTestUtils.invokeMethod(serverService, "unregisterGameServices");
  }
}