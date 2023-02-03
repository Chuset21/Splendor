package hexanome.fourteen.server.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;

import hexanome.fourteen.server.control.form.ClaimNobleForm;
import hexanome.fourteen.server.control.form.LaunchGameForm;
import hexanome.fourteen.server.control.form.PurchaseCardForm;
import hexanome.fourteen.server.control.form.ReserveCardForm;
import hexanome.fourteen.server.control.form.TakeGemsForm;
import hexanome.fourteen.server.model.User;
import hexanome.fourteen.server.model.board.GameBoard;
import hexanome.fourteen.server.model.board.Noble;
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
    final Player player = new Player("test");
    final GameBoard board =
        new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(player), "x", null);
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
    assertEquals("card chosen for purchase is not valid", response.getBody());

    Gems cardCost = new Gems();
    cardCost.put(GemColor.GREEN, 2);
    cardCost.put(GemColor.WHITE, 2);
    Gems substitutedGems = new Gems();
    substitutedGems.put(GemColor.GOLD, 2);
    card = new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, 1, GemColor.BLACK);
    purchaseCardForm = new PurchaseCardForm(card, new Gems(), substitutedGems);
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot substitute gold gems for gold gems", response.getBody());

    substitutedGems = new Gems();
    substitutedGems.put(GemColor.BLUE, 2);
    purchaseCardForm = new PurchaseCardForm(card, new Gems(), substitutedGems);
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("substituting amount not equal to gold gems", response.getBody());

    Gems paymentGems = new Gems();
    paymentGems.put(GemColor.BLACK, 4);
    purchaseCardForm = new PurchaseCardForm(card, paymentGems, new Gems());
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("not enough gems", response.getBody());

    paymentGems = new Gems();
    paymentGems.put(GemColor.BLUE, 3);
    paymentGems.put(GemColor.WHITE, 1);
    purchaseCardForm = new PurchaseCardForm(card, paymentGems, new Gems());
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card cost does not match payment", response.getBody());

    paymentGems = new Gems();
    paymentGems.put(GemColor.GOLD, 3);
    paymentGems.put(GemColor.WHITE, 1);
    substitutedGems = new Gems();
    substitutedGems.put(GemColor.WHITE, 1);
    substitutedGems.put(GemColor.GREEN, 2);
    purchaseCardForm = new PurchaseCardForm(card, paymentGems, substitutedGems);
    final Gems previousPlayersGems = new Gems(player.hand().gems());
    response = gameHandlerController.purchaseCard("", "token", purchaseCardForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(response.getBody());
    assertEquals(previousPlayersGems.get(GemColor.GOLD) - paymentGems.get(GemColor.GOLD),
        player.hand().gems().getOrDefault(GemColor.GOLD, 0));
    assertEquals(previousPlayersGems.get(GemColor.WHITE) - paymentGems.get(GemColor.WHITE),
        player.hand().gems().getOrDefault(GemColor.WHITE, 0));
  }

  @Test
  public void testReserveCard() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Player player = new Player("test");
    GameBoard board = new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(player), "x", null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);

    ResponseEntity<String> response = gameHandlerController.reserveCard("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("invalid access token", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("x");

    response = gameHandlerController.reserveCard("x", "token", null);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.reserveCard("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("player is not part of this game", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");

    Gems cardCost = new Gems();
    cardCost.put(GemColor.GREEN, 2);
    cardCost.put(GemColor.WHITE, 2);
    Card card = new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, 1, GemColor.BLACK);
    ReserveCardForm reserveCardForm = new ReserveCardForm(card, GemColor.WHITE, false);

    player.hand().gems().clear();

    response = gameHandlerController.reserveCard("", "token", reserveCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot get rid of a gem if you have less than 10 gems", response.getBody());

    player.hand().gems().put(GemColor.BLUE, 10);

    response = gameHandlerController.reserveCard("", "token", reserveCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot get rid of a gem that you do not have", response.getBody());

    player.hand().gems().put(GemColor.WHITE, 1);
    player.hand().gems().put(GemColor.BLUE, 9);
    board.availableGems().clear();

    response = gameHandlerController.reserveCard("", "token", reserveCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot get rid of a gem if there are no gold gems in the bank",
        response.getBody());

    board.availableGems().put(GemColor.GOLD, 1);
    player.hand().reservedCards().add(null);
    player.hand().reservedCards().add(null);
    player.hand().reservedCards().add(null);
    // 3 reserved cards

    response = gameHandlerController.reserveCard("", "token", null);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot reserve more than 3 cards", response.getBody());

    player.hand().reservedCards().add(null);
    // 4 reserved cards

    response = gameHandlerController.reserveCard("", "token", null);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot reserve more than 3 cards", response.getBody());

    player.hand().reservedCards().remove(null);
    player.hand().reservedCards().remove(null);
    // 2 reserved cards

    card = new StandardCard(0, new Gems(), CardLevel.ONE, Expansion.STANDARD, 1, GemColor.BLACK);
    reserveCardForm = new ReserveCardForm(card, GemColor.WHITE, false);

    response = gameHandlerController.reserveCard("", "token", reserveCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card chosen for reservation is not valid", response.getBody());

    card = new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, 1, GemColor.BLACK);
    reserveCardForm = new ReserveCardForm(card, GemColor.WHITE, false);

    response = gameHandlerController.reserveCard("", "token", reserveCardForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, player.hand().gems().get(GemColor.GOLD));
    assertFalse(board.availableGems().containsKey(GemColor.GOLD));
    assertFalse(player.hand().gems().containsKey(GemColor.WHITE));
    assertEquals(1, board.availableGems().get(GemColor.WHITE));
    assertEquals(3, player.hand().reservedCards().size());
    assertTrue(player.hand().reservedCards().remove(card));
    assertEquals(2, player.hand().reservedCards().size());

    player.hand().reservedCards().remove(null);
    player.hand().reservedCards().remove(null);
    // 0 reserved cards

    board = new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(player), "x", null);
    gameManager.put("", board);
    board.availableGems().clear();
    board.availableGems().put(GemColor.GOLD, 2);
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.BLUE, 8);
    reserveCardForm = new ReserveCardForm(card, null, false);

    response = gameHandlerController.reserveCard("", "token", reserveCardForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, player.hand().gems().get(GemColor.GOLD));
    assertEquals(1, board.availableGems().get(GemColor.GOLD));
    assertEquals(1, player.hand().reservedCards().size());
    assertTrue(player.hand().reservedCards().remove(card));
    assertTrue(player.hand().reservedCards().isEmpty());

    board = new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(player), "x", null);
    gameManager.put("", board);
    board.availableGems().clear();
    board.availableGems().put(GemColor.GOLD, 2);
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.BLUE, 8);
    reserveCardForm = new ReserveCardForm(null, null, false);

    response = gameHandlerController.reserveCard("", "token", reserveCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card cannot be null", response.getBody());

    board = new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(player), "x", null);
    ReflectionTestUtils.setField(board, "cards", new HashSet<>());
    gameManager.put("", board);
    board.availableGems().clear();
    board.availableGems().put(GemColor.GOLD, 2);
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.BLUE, 8);
    reserveCardForm = new ReserveCardForm(card, null, true);

    response = gameHandlerController.reserveCard("", "token", reserveCardForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("not enough cards in the deck to reserve a face down card", response.getBody());

    board = new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(player), "x", null);
    gameManager.put("", board);
    board.availableGems().clear();
    board.availableGems().put(GemColor.GOLD, 2);
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.BLUE, 8);
    reserveCardForm = new ReserveCardForm(card, null, true);

    response = gameHandlerController.reserveCard("", "token", reserveCardForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, player.hand().gems().get(GemColor.GOLD));
    assertEquals(1, board.availableGems().get(GemColor.GOLD));
    assertFalse(player.hand().reservedCards().remove(card));
    assertEquals(1, player.hand().reservedCards().size());
  }

  @Test
  public void testTakeGems() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Player player = new Player("test");
    GameBoard board =
        new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(player, new Player("test2")), "x",
            null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);

    ResponseEntity<String> response = gameHandlerController.takeGems("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("invalid access token", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("x");

    response = gameHandlerController.takeGems("x", "token", null);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.takeGems("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("player is not part of this game", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");

    TakeGemsForm takeGemsForm = new TakeGemsForm(null, null);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("gems to take cannot be null", response.getBody());

    final Gems gemsToTake = new Gems();
    gemsToTake.put(GemColor.GOLD, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take gold gems", response.getBody());

    gemsToTake.clear();
    gemsToTake.put(GemColor.WHITE, 4);
    final Gems gemsToRemove = new Gems();
    takeGemsForm = new TakeGemsForm(gemsToTake, gemsToRemove);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take more than 3 gems", response.getBody());

    gemsToTake.put(GemColor.WHITE, 2);
    board.availableGems().clear();

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("not enough gems in the bank", response.getBody());

    gemsToTake.put(GemColor.WHITE, 2);
    board.availableGems().put(GemColor.WHITE, 5);
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 10);
    gemsToRemove.put(GemColor.BLUE, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, gemsToRemove);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot remove gems that you do not own", response.getBody());

    takeGemsForm = new TakeGemsForm(gemsToTake, null);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("can only have a maximum of 10 gems", response.getBody());

    gemsToRemove.clear();
    gemsToRemove.put(GemColor.WHITE, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, gemsToRemove);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("can only have a maximum of 10 gems", response.getBody());

    player.hand().gems().put(GemColor.GOLD, 6);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot remove gems if not necessary", response.getBody());

    gemsToRemove.put(GemColor.GOLD, 2);
    player.hand().gems().put(GemColor.GOLD, 10);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot remove gems and be left with less than 10", response.getBody());

    player.hand().gems().put(GemColor.GOLD, 7);
    gemsToTake.put(GemColor.WHITE, 3);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take 3 gems of one color", response.getBody());

    gemsToTake.put(GemColor.WHITE, 2);
    board.availableGems().put(GemColor.WHITE, 3);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(
        "cannot take 2 same color gems, there are less than 4 gems of that color in the bank",
        response.getBody());

    gemsToTake.put(GemColor.WHITE, 1);
    board.availableGems().put(GemColor.WHITE, 4);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take 1 gem if it is possible to take more", response.getBody());

    board.availableGems().put(GemColor.WHITE, 3);
    board.availableGems().put(GemColor.BLUE, 3);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take 1 gem if it is possible to take more", response.getBody());

    gemsToTake.put(GemColor.BLUE, 2);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take more than one gem of each gem", response.getBody());

    gemsToTake.put(GemColor.BLUE, 1);
    board.availableGems().put(GemColor.BLACK, 3);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(
        "cannot take 1 gem of each for 2 gem colors if it is possible to take from 3 colors",
        response.getBody());

    // 1 gem of each for two different colors, when there are only 2 colors available
    gemsToTake.put(GemColor.BLUE, 1);
    board.availableGems().remove(GemColor.BLACK);
    Gems previousOwnedGems = new Gems(player.hand().gems());
    Gems previousBank = new Gems(board.availableGems());

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(previousOwnedGems.getOrDefault(GemColor.BLUE, 0) + 1,
        player.hand().gems().get(GemColor.BLUE));
    assertEquals(previousOwnedGems.getOrDefault(GemColor.WHITE, 0) + 1,
        player.hand().gems().get(GemColor.WHITE));
    assertEquals(previousBank.get(GemColor.BLUE) - 1,
        board.availableGems().getOrDefault(GemColor.BLUE, 0));
    assertEquals(previousBank.get(GemColor.WHITE) - 1,
        board.availableGems().getOrDefault(GemColor.WHITE, 0));

    // 2 gems of one color, when there are 4 or more of that given color available
    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToTake.put(GemColor.WHITE, 2);
    board.availableGems().put(GemColor.WHITE, 4);
    previousOwnedGems = new Gems(player.hand().gems());
    previousBank = new Gems(board.availableGems());

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(previousOwnedGems.getOrDefault(GemColor.WHITE, 0) + 2,
        player.hand().gems().get(GemColor.WHITE));
    assertEquals(previousBank.get(GemColor.WHITE) - 2,
        board.availableGems().getOrDefault(GemColor.WHITE, 0));

    // 1 gem of one color, when there are less than 4 of the given gem color and this is the only color available
    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToTake.put(GemColor.WHITE, 1);
    board.availableGems().put(GemColor.WHITE, 3);
    previousOwnedGems = new Gems(player.hand().gems());
    previousBank = new Gems(board.availableGems());

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(previousOwnedGems.getOrDefault(GemColor.WHITE, 0) + 1,
        player.hand().gems().get(GemColor.WHITE));
    assertEquals(previousBank.get(GemColor.WHITE) - 1,
        board.availableGems().getOrDefault(GemColor.WHITE, 0));

    // 3 gems of different color, when there are 3 different colors available
    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToTake.put(GemColor.WHITE, 1);
    gemsToTake.put(GemColor.BLUE, 1);
    gemsToTake.put(GemColor.BLACK, 1);
    board.availableGems().put(GemColor.WHITE, 1);
    board.availableGems().put(GemColor.BLUE, 2);
    board.availableGems().put(GemColor.BLACK, 3);
    previousOwnedGems = new Gems(player.hand().gems());
    previousBank = new Gems(board.availableGems());

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(previousOwnedGems.getOrDefault(GemColor.WHITE, 0) + 1,
        player.hand().gems().get(GemColor.WHITE));
    assertEquals(previousBank.get(GemColor.WHITE) - 1,
        board.availableGems().getOrDefault(GemColor.WHITE, 0));
    assertEquals(previousOwnedGems.getOrDefault(GemColor.BLUE, 0) + 1,
        player.hand().gems().get(GemColor.BLUE));
    assertEquals(previousBank.get(GemColor.BLUE) - 1,
        board.availableGems().getOrDefault(GemColor.BLUE, 0));
    assertEquals(previousOwnedGems.getOrDefault(GemColor.BLACK, 0) + 1,
        player.hand().gems().get(GemColor.BLACK));
    assertEquals(previousBank.get(GemColor.BLACK) - 1,
        board.availableGems().getOrDefault(GemColor.BLACK, 0));

    // The player is left with more than 10 gems at the end of the turn and removes the gems that they took (stupid)
    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToRemove.clear();
    gemsToTake.put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.BLACK, 10);
    board.availableGems().put(GemColor.WHITE, 4);
    gemsToRemove.put(GemColor.WHITE, 2);
    previousOwnedGems = new Gems(player.hand().gems());
    previousBank = new Gems(board.availableGems());
    takeGemsForm = new TakeGemsForm(gemsToTake, gemsToRemove);

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(previousOwnedGems.getOrDefault(GemColor.WHITE, 0),
        player.hand().gems().getOrDefault(GemColor.WHITE, 0));
    assertEquals(previousBank.get(GemColor.WHITE),
        board.availableGems().getOrDefault(GemColor.WHITE, 0));

    // The player is left with more than 10 gems and gets rid of previously owned gems
    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToRemove.clear();
    gemsToTake.put(GemColor.WHITE, 1);
    gemsToTake.put(GemColor.RED, 1);
    gemsToTake.put(GemColor.GREEN, 1);
    player.hand().gems().put(GemColor.BLACK, 7);
    player.hand().gems().put(GemColor.GOLD, 2);
    board.availableGems().put(GemColor.WHITE, 4);
    board.availableGems().put(GemColor.RED, 1);
    board.availableGems().put(GemColor.GREEN, 1);
    gemsToRemove.put(GemColor.BLACK, 1);
    gemsToRemove.put(GemColor.GOLD, 1);
    previousOwnedGems = new Gems(player.hand().gems());
    previousBank = new Gems(board.availableGems());

    response = gameHandlerController.takeGems("", "token", takeGemsForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(previousOwnedGems.getOrDefault(GemColor.WHITE, 0) + 1,
        player.hand().gems().get(GemColor.WHITE));
    assertEquals(previousBank.get(GemColor.WHITE) - 1,
        board.availableGems().getOrDefault(GemColor.WHITE, 0));
    assertEquals(previousOwnedGems.getOrDefault(GemColor.RED, 0) + 1,
        player.hand().gems().get(GemColor.RED));
    assertEquals(previousBank.get(GemColor.RED) - 1,
        board.availableGems().getOrDefault(GemColor.RED, 0));
    assertEquals(previousOwnedGems.getOrDefault(GemColor.GREEN, 0) + 1,
        player.hand().gems().get(GemColor.GREEN));
    assertEquals(previousBank.get(GemColor.GREEN) - 1,
        board.availableGems().getOrDefault(GemColor.GREEN, 0));
    assertEquals(previousOwnedGems.get(GemColor.BLACK) - 1,
        player.hand().gems().getOrDefault(GemColor.BLACK, 0));
    assertEquals(previousBank.getOrDefault(GemColor.BLACK, 0) + 1,
        board.availableGems().get(GemColor.BLACK));
    assertEquals(previousOwnedGems.get(GemColor.GOLD) - 1,
        player.hand().gems().getOrDefault(GemColor.GOLD, 0));
    assertEquals(previousBank.getOrDefault(GemColor.GOLD, 0) + 1,
        board.availableGems().get(GemColor.GOLD));
  }

  @Test
  public void testClaimNoble() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Player player = new Player("test");
    GameBoard board =
        new GameBoard(new HashSet<>(), new HashSet<>(), Set.of(player, new Player("test2")), "x",
            null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);

    ResponseEntity<String> response = gameHandlerController.claimNoble("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("invalid access token", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("x");

    response = gameHandlerController.claimNoble("x", "token", null);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.claimNoble("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("player is not part of this game", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");

    ClaimNobleForm claimNobleForm = new ClaimNobleForm(null);

    response = gameHandlerController.claimNoble("", "token", claimNobleForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("noble to claim cannot be null", response.getBody());

    final Noble nobleToClaim = new Noble(2, new Gems());
    claimNobleForm = new ClaimNobleForm(nobleToClaim);

    response = gameHandlerController.claimNoble("", "token", claimNobleForm);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("noble to claim is not available", response.getBody());

    player.hand().setReservedNoble(nobleToClaim);
    assertNotNull(player.hand().reservedNoble());

    response = gameHandlerController.claimNoble("", "token", claimNobleForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(player.hand().reservedNoble());
    assertEquals(Set.of(nobleToClaim), player.hand().visitedNobles());

    player.hand().visitedNobles().remove(nobleToClaim);
    assertTrue(player.hand().visitedNobles().isEmpty());
    board.availableNobles().add(nobleToClaim);

    response = gameHandlerController.claimNoble("", "token", claimNobleForm);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(board.availableNobles().contains(nobleToClaim));
    assertEquals(Set.of(nobleToClaim), player.hand().visitedNobles());

    player.hand().visitedNobles().remove(nobleToClaim);
    assertTrue(player.hand().visitedNobles().isEmpty());
  }
}
