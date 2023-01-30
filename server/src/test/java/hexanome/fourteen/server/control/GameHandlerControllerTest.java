package hexanome.fourteen.server.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;

import hexanome.fourteen.server.model.User;
import hexanome.fourteen.server.model.board.GameBoard;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.card.CardLevel;
import hexanome.fourteen.server.model.board.card.StandardCard;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.expansion.StringExpansionMapper;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import hexanome.fourteen.server.model.board.player.UserPlayerMapper;
import hexanome.fourteen.server.model.clientmapper.ServerToClientBoardGameMapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

@TestInstance(PER_CLASS)
public class GameHandlerControllerTest {

  private static GsonInstance gsonInstance;
  private LobbyServiceCaller lobbyService;
  private GameHandlerController gameHandlerController;
  private static User user1;
  private static User user2;

  @BeforeAll
  public static void setUp() {
    gsonInstance = new GsonInstance();
    ReflectionTestUtils.invokeMethod(gsonInstance, "initGson");

    user1 = new User();
    user2 = new User();
    ReflectionTestUtils.setField(user1, "name", "user1");
    ReflectionTestUtils.setField(user2, "name", "user2");
    ReflectionTestUtils.setField(user1, "preferredColour", "test");
    ReflectionTestUtils.setField(user2, "preferredColour", "test");
  }

  @BeforeEach
  public void init() {
    lobbyService = mock(LobbyServiceCaller.class);

    Mockito.when(lobbyService.getUsername("user1")).thenReturn(null);
    Mockito.when(lobbyService.getUsername("user2")).thenReturn("x");

    gameHandlerController =
        new GameHandlerController(lobbyService, new UserPlayerMapper(), new StringExpansionMapper(),
            new ServerToClientBoardGameMapper(), gsonInstance);
  }

  @Test
  public void testLaunchGame() {
    final LaunchGameForm launchGameForm = new LaunchGameForm();

    ReflectionTestUtils.setField(launchGameForm, "creator", "test");
    ReflectionTestUtils.setField(launchGameForm, "gameType", Expansion.STANDARD.name());
    ReflectionTestUtils.setField(launchGameForm, "players", new User[] {user1, user2});
    ReflectionTestUtils.setField(launchGameForm, "saveGame", "test");

    ResponseEntity<String> response = gameHandlerController.launchGame("gameid", launchGameForm);

    assertNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testDeleteGame() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    gameManager.put("x",
        new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(new Player("test")), "x", null));
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);

    ResponseEntity<String> response = gameHandlerController.deleteGame("???");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.deleteGame("x");
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testRetrieveGame() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final GameBoard board =
        new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(new Player("player2")), "x", null);
    gameManager.put("test", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);

    ResponseEntity<String> response = gameHandlerController.retrieveGame("", "user1");
    assertEquals("invalid access token", response.getBody());
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    response = gameHandlerController.retrieveGame("", "user2");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.retrieveGame("test", "user2");
    assertEquals("player is not part of this game", response.getBody());
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    Mockito.when(lobbyService.getUsername("user2")).thenReturn("player2");
    response = gameHandlerController.retrieveGame("test", "user2");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(gsonInstance.gson.toJson(new ServerToClientBoardGameMapper().map(board)),
        response.getBody());
  }

  @Test
  public void testPurchaseCard() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final GameBoard board =
        new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(new Player("test")), "x", null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);

    ResponseEntity<String> response = gameHandlerController.purchaseCard("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("invalid access token", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("x");

    response = gameHandlerController.purchaseCard("x", "token", null);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.purchaseCard("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("player is not part of this game", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");

    Card card =
        new StandardCard(0, new Gems(), CardLevel.ONE, Expansion.STANDARD, 1, GemColor.BLACK);
    PurchaseCardForm purchaseCardForm = new PurchaseCardForm(card, new Gems(), new Gems());
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card chosen for purchase is not valid",
        response.getBody());

    Gems cardCost = new Gems();
    cardCost.put(GemColor.GREEN, 2);
    cardCost.put(GemColor.WHITE, 2);
    Gems substitutedGems = new Gems();
    substitutedGems.put(GemColor.GOLD, 2);
    card =
        new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, 1, GemColor.BLACK);
    purchaseCardForm = new PurchaseCardForm(card, new Gems(), substitutedGems);
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot substitute gold gems for gold gems",
        response.getBody());

    substitutedGems = new Gems();
    substitutedGems.put(GemColor.BLUE, 2);
    purchaseCardForm = new PurchaseCardForm(card, new Gems(), substitutedGems);
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("substituting amount not equal to gold gems",
        response.getBody());

    Gems paymentGems = new Gems();
    paymentGems.put(GemColor.BLACK, 4);
    purchaseCardForm = new PurchaseCardForm(card, paymentGems, new Gems());
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("not enough gems",
        response.getBody());

    paymentGems = new Gems();
    paymentGems.put(GemColor.BLUE, 3);
    paymentGems.put(GemColor.WHITE, 1);
    purchaseCardForm = new PurchaseCardForm(card, paymentGems, new Gems());
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card cost does not match payment",
        response.getBody());

    paymentGems = new Gems();
    paymentGems.put(GemColor.GOLD, 3);
    paymentGems.put(GemColor.WHITE, 1);
    substitutedGems = new Gems();
    substitutedGems.put(GemColor.WHITE, 1);
    substitutedGems.put(GemColor.GREEN, 2);
    purchaseCardForm = new PurchaseCardForm(card, paymentGems, substitutedGems);
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(response.getBody());
  }
}
