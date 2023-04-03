package hexanome.fourteen.server.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import hexanome.fourteen.server.control.form.ClaimCityForm;
import hexanome.fourteen.server.control.form.ClaimNobleForm;
import hexanome.fourteen.server.control.form.LaunchGameForm;
import hexanome.fourteen.server.control.form.PurchaseCardForm;
import hexanome.fourteen.server.control.form.ReserveCardForm;
import hexanome.fourteen.server.control.form.TakeGemsForm;
import hexanome.fourteen.server.control.form.payment.CardPayment;
import hexanome.fourteen.server.control.form.payment.GemPayment;
import hexanome.fourteen.server.control.form.payment.Payment;
import hexanome.fourteen.server.control.form.tradingpost.TradingPostTakeGem;
import hexanome.fourteen.server.model.User;
import hexanome.fourteen.server.model.board.City;
import hexanome.fourteen.server.model.board.GameBoard;
import hexanome.fourteen.server.model.board.Noble;
import hexanome.fourteen.server.model.board.card.Bonus;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.card.CardLevel;
import hexanome.fourteen.server.model.board.card.DoubleBonusCard;
import hexanome.fourteen.server.model.board.card.GoldGemCard;
import hexanome.fourteen.server.model.board.card.ReserveNobleCard;
import hexanome.fourteen.server.model.board.card.SacrificeCard;
import hexanome.fourteen.server.model.board.card.SatchelCard;
import hexanome.fourteen.server.model.board.card.StandardCard;
import hexanome.fourteen.server.model.board.card.WaterfallCard;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import hexanome.fourteen.server.model.board.tradingposts.TradingPostsEnum;
import hexanome.fourteen.server.model.clientmapper.ServerToClientBoardGameMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.context.request.async.DeferredResult;

@TestInstance(PER_CLASS)
public class GameHandlerControllerTest {

  private static GsonInstance gsonInstance;
  private LobbyServiceCaller lobbyService;
  private SavedGamesService saveGameManager;
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
    saveGameManager = mock(SavedGamesService.class);

    Mockito.when(lobbyService.getUsername("user1")).thenReturn(null);
    Mockito.when(lobbyService.getUsername("user2")).thenReturn("x");

    gameHandlerController =
        new GameHandlerController(30000L, lobbyService, gsonInstance,
            saveGameManager,
            new ServerService(gsonInstance, lobbyService, "", ""));
  }

  @Test
  public void testLaunchGame() {
    final LaunchGameForm launchGameForm = new LaunchGameForm();

    ReflectionTestUtils.setField(launchGameForm, "creator", "test");
    ReflectionTestUtils.setField(launchGameForm, "gameType", GameServiceName.BASE.name());
    ReflectionTestUtils.setField(launchGameForm, "players", new User[] {user1, user2});
    ReflectionTestUtils.setField(launchGameForm, "saveGame", "XYZ45");
    ResponseEntity<String> response =
        gameHandlerController.launchGame("gameid", gsonInstance.gson.toJson(launchGameForm));

    assertNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    final String saveGame = "XYZ45";
    Mockito.when(saveGameManager.getGame(saveGame)).thenReturn(
        new GameBoard(Set.of(Expansion.STANDARD), Set.of(new User("user1")), "gameid", ""));

    response = gameHandlerController.launchGame("gameid", gsonInstance.gson.toJson(launchGameForm));

    assertNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testDeleteGame() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final GameBoard board = gameManager.put("x",
        new GameBoard(new HashSet<>(), Set.of(new User("test")), "x", null));
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);
    Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers =
        (Map<String, BroadcastContentManager<GameBoard>>) ReflectionTestUtils.getField(
            gameHandlerController,
            "gameSpecificBroadcastManagers");
    assertNotNull(gameSpecificBroadcastManagers);
    gameSpecificBroadcastManagers.put("x", new BroadcastContentManager<>(board));

    ResponseEntity<String> response = gameHandlerController.deleteGame("???");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.deleteGame("x");
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testRetrieveGame() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final GameBoard board =
        new GameBoard(new HashSet<>(), Set.of(new User("player2")), "x", null);
    gameManager.put("test", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);
    Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers =
        (Map<String, BroadcastContentManager<GameBoard>>) ReflectionTestUtils.getField(
            gameHandlerController,
            "gameSpecificBroadcastManagers");
    assertNotNull(gameSpecificBroadcastManagers);
    gameSpecificBroadcastManagers.put("test", new BroadcastContentManager<>(board));

    DeferredResult<ResponseEntity<String>> result =
        gameHandlerController.retrieveGame("", "user1", null);
    ResponseEntity<String> response = (ResponseEntity<String>) result.getResult();
    assertNotNull(response);
    assertEquals("invalid access token", response.getBody());
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    result = gameHandlerController.retrieveGame("", "user2", null);
    response = (ResponseEntity<String>) result.getResult();
    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    result = gameHandlerController.retrieveGame("test", "user2", null);
    response = (ResponseEntity<String>) result.getResult();
    assertNotNull(response);
    assertEquals("player is not part of this game", response.getBody());
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    Mockito.when(lobbyService.getUsername("user2")).thenReturn("player2");
    result = gameHandlerController.retrieveGame("test", "user2", null);
    response = (ResponseEntity<String>) result.getResult();
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(gsonInstance.gson.toJson(new ServerToClientBoardGameMapper().map(board)),
        response.getBody());
  }

  @Test
  public void testPurchaseCard() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Set<Expansion> expansions = GameServiceName.getExpansions(GameServiceName.ALL);
    final Player player = new Player("test", expansions);
    GameBoard board =
        new GameBoard(expansions, Set.of(player, new Player("test2", expansions)), "x",
            null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);

    ResponseEntity<String> response = gameHandlerController.purchaseCard("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("invalid access token", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("x");
    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }

    response = gameHandlerController.purchaseCard("x", "token", null);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.purchaseCard("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("player is not part of this game", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");
    PurchaseCardForm purchaseCardForm = new PurchaseCardForm();

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card cannot be null", response.getBody());

    Card cardToPurchase = new StandardCard();
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, null, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card is not reserved", response.getBody());

    purchaseCardForm = new PurchaseCardForm(cardToPurchase, null, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("payment cannot be null", response.getBody());

    TradingPostTakeGem tradingPostTakeGem = new TradingPostTakeGem();
    purchaseCardForm =
        new PurchaseCardForm(cardToPurchase, new CardPayment(), false, tradingPostTakeGem);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take an extra gem if you don't own the power one trading post",
        response.getBody());

    purchaseCardForm =
        new PurchaseCardForm(cardToPurchase, new CardPayment(), false, null);
    player.hand().tradingPosts().put(TradingPostsEnum.BONUS_GEM_WITH_CARD, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("must take an extra gem if you own the power one trading post",
        response.getBody());

    tradingPostTakeGem = new TradingPostTakeGem();
    purchaseCardForm =
        new PurchaseCardForm(cardToPurchase, new CardPayment(), false, tradingPostTakeGem);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("gemToTake cannot be null when there are gems available to take after a purchase",
        response.getBody());

    tradingPostTakeGem = new TradingPostTakeGem(GemColor.GOLD, GemColor.BLUE);
    purchaseCardForm =
        new PurchaseCardForm(cardToPurchase, new CardPayment(), false, tradingPostTakeGem);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("gemToRemove must be null if you don't have 10 gems after a purchase",
        response.getBody());

    tradingPostTakeGem = new TradingPostTakeGem(GemColor.GOLD, null);
    purchaseCardForm =
        new PurchaseCardForm(cardToPurchase, new CardPayment(), false, tradingPostTakeGem);
    player.hand().gems().put(GemColor.RED, 10);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("gemToRemove cannot be null when you have 10 gems after a purchase",
        response.getBody());

    tradingPostTakeGem = new TradingPostTakeGem(GemColor.GOLD, null);
    purchaseCardForm =
        new PurchaseCardForm(cardToPurchase, new CardPayment(), false, tradingPostTakeGem);
    player.hand().gems().clear();

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take a gold gem", response.getBody());

    tradingPostTakeGem = new TradingPostTakeGem(GemColor.BLUE, null);
    purchaseCardForm =
        new PurchaseCardForm(cardToPurchase, new CardPayment(), false, tradingPostTakeGem);
    board.availableGems().clear();

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("gem color: %s is not available after the purchase".formatted(
            tradingPostTakeGem.gemToTake()),
        response.getBody());

    tradingPostTakeGem = new TradingPostTakeGem(GemColor.BLUE, GemColor.GOLD);
    purchaseCardForm =
        new PurchaseCardForm(cardToPurchase, new CardPayment(), false, tradingPostTakeGem);
    board.availableGems().put(GemColor.BLUE, 1);
    player.hand().gems().put(GemColor.BLACK, 10);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(
        "gem color: %s is not owned after the purchase and thus cannot be removed".formatted(
            tradingPostTakeGem.gemToRemove()), response.getBody());

    purchaseCardForm =
        new PurchaseCardForm(cardToPurchase, new CardPayment(), false, tradingPostTakeGem);
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.BLACK, 9);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card chosen for purchase is not valid", response.getBody());
  }

  @Test
  public void testPurchaseCardWithCardPayment() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Set<Expansion> expansions = GameServiceName.getExpansions(GameServiceName.ALL);
    final Player player = new Player("test", expansions);
    GameBoard board =
        new GameBoard(expansions, Set.of(player, new Player("test2", expansions)), "x",
            null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);
    Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers =
        (Map<String, BroadcastContentManager<GameBoard>>) ReflectionTestUtils.getField(
            gameHandlerController,
            "gameSpecificBroadcastManagers");
    assertNotNull(gameSpecificBroadcastManagers);
    gameSpecificBroadcastManagers.put("x", new BroadcastContentManager<>(board));
    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");

    Card cardToPurchase = new StandardCard();
    player.hand().reservedCards().add(cardToPurchase);
    Payment payment = new CardPayment();
    PurchaseCardForm purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    ResponseEntity<String> response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("can only pay for a card with cards if it is a sacrifice card",
        response.getBody());
    player.hand().reservedCards().clear();

    cardToPurchase =
        new SacrificeCard(3, new Gems(), CardLevel.THREE, Expansion.ORIENT, GemColor.BLUE, GemColor.BLUE);
    player.hand().reservedCards().add(cardToPurchase);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("both cards to sacrifice cannot be null", response.getBody());
    player.hand().reservedCards().clear();

    Card cardToSacrifice1 = new StandardCard();
    player.hand().reservedCards().add(cardToPurchase);
    payment = new CardPayment(cardToSacrifice1, null);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot sacrifice a card that you do not own", response.getBody());
    player.hand().reservedCards().clear();

    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("must sacrifice a double bonus card or a satchel card containing another card if "
                 + "you're only sacrificing one card", response.getBody());
    player.hand().reservedCards().clear();
    player.hand().purchasedCards().clear();

    cardToSacrifice1 = new DoubleBonusCard();
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    player.hand().purchasedCards().add(
        new SatchelCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT,
            new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE)));
    payment = new CardPayment(cardToSacrifice1, null);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("must sacrifice a satchel card if possible", response.getBody());
    player.hand().reservedCards().clear();
    player.hand().purchasedCards().clear();

    cardToSacrifice1 =
        new DoubleBonusCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT, GemColor.RED);
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    payment = new CardPayment(cardToSacrifice1, null);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("sacrifice card discount color must equal purchased card sacrifice color",
        response.getBody());
    player.hand().reservedCards().clear();
    player.hand().purchasedCards().clear();

    cardToSacrifice1 =
        new DoubleBonusCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT, GemColor.BLUE);
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    payment = new CardPayment(cardToSacrifice1, null);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);
    player.hand().gemDiscounts().put(GemColor.BLUE, 2);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(player.hand().reservedCards().isEmpty());
    assertTrue(player.hand().purchasedCards().contains(cardToPurchase));
    assertFalse(player.hand().purchasedCards().contains(cardToSacrifice1));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.SINGLE.getValue(), player.hand().gemDiscounts()
        .getOrDefault(((SacrificeCard) cardToPurchase).discountColor(), 0));
    player.hand().purchasedCards().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);

    // test with satchel card now
    Card cardToAttach =
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.RED);
    cardToSacrifice1 =
        new SatchelCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT, cardToAttach);
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    payment = new CardPayment(cardToSacrifice1, null);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    if (!board.isPlayerTurn("test")) {
      response = gameHandlerController.purchaseCard("", "token",
              gsonInstance.gson.toJson(purchaseCardForm));
      assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
      assertEquals("cannot take an action outside of your turn", response.getBody());
      board.nextTurn();
    }
    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("discount color must match the purchased card's discount color",
        response.getBody());
    player.hand().reservedCards().clear();
    player.hand().purchasedCards().clear();

    cardToAttach =
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE);
    cardToSacrifice1 =
        new SatchelCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT, cardToAttach);
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    payment = new CardPayment(cardToSacrifice1, null);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);
    player.hand().gemDiscounts().put(GemColor.BLUE, 2);
    player.hand().setPrestigePoints(cardToAttach.prestigePoints());

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(player.hand().reservedCards().isEmpty());
    assertTrue(player.hand().purchasedCards().contains(cardToPurchase));
    assertFalse(player.hand().purchasedCards().contains(cardToSacrifice1));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.SINGLE.getValue(), player.hand().gemDiscounts()
        .getOrDefault(((SacrificeCard) cardToPurchase).discountColor(), 0));
    player.hand().purchasedCards().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);

    cardToAttach =
        new DoubleBonusCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE);
    cardToSacrifice1 =
        new SatchelCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT, cardToAttach);
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    payment = new CardPayment(cardToSacrifice1, null);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);
    player.hand().gemDiscounts().put(GemColor.BLUE, 4);
    player.hand().setPrestigePoints(cardToAttach.prestigePoints());

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(player.hand().reservedCards().isEmpty());
    assertTrue(player.hand().purchasedCards().contains(cardToPurchase));
    assertFalse(player.hand().purchasedCards().contains(cardToSacrifice1));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.SINGLE.getValue() + 1, player.hand().gemDiscounts()
        .getOrDefault(((SacrificeCard) cardToPurchase).discountColor(), 0));
    player.hand().purchasedCards().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);

    // Now test with two cards to sacrifice
    cardToSacrifice1 =
        new DoubleBonusCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT, GemColor.RED);
    Card cardToSacrifice2 = new WaterfallCard();
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    payment = new CardPayment(cardToSacrifice1, cardToSacrifice2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("you must own the cards you're sacrificing", response.getBody());
    player.hand().reservedCards().clear();
    player.hand().purchasedCards().clear();

    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    player.hand().purchasedCards().add(cardToSacrifice2);
    payment = new CardPayment(cardToSacrifice1, cardToSacrifice2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot sacrifice a double bonus card or satchel card "
                 + "if you're sacrificing two cards", response.getBody());
    player.hand().reservedCards().clear();
    player.hand().purchasedCards().clear();

    cardToSacrifice1 = new SatchelCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT,
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE));
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    player.hand().purchasedCards().add(cardToSacrifice2);
    payment = new CardPayment(cardToSacrifice1, cardToSacrifice2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot sacrifice a double bonus card or satchel card "
                 + "if you're sacrificing two cards", response.getBody());
    player.hand().reservedCards().clear();
    player.hand().purchasedCards().clear();

    cardToSacrifice1 = new StandardCard();
    player.hand().purchasedCards().add(
        new SatchelCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT,
            new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE)));
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    player.hand().purchasedCards().add(cardToSacrifice2);
    payment = new CardPayment(cardToSacrifice1, cardToSacrifice2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("must sacrifice a satchel card if possible", response.getBody());
    player.hand().reservedCards().clear();
    player.hand().purchasedCards().clear();

    cardToSacrifice1 =
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE);
    cardToSacrifice2 =
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.RED);
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    player.hand().purchasedCards().add(cardToSacrifice2);
    payment = new CardPayment(cardToSacrifice1, cardToSacrifice2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("discount color must match the purchased card's discount color",
        response.getBody());
    player.hand().reservedCards().clear();
    player.hand().purchasedCards().clear();

    cardToSacrifice1 =
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.RED);
    cardToSacrifice2 =
        new StandardCard(2, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE);
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    player.hand().purchasedCards().add(cardToSacrifice2);
    payment = new CardPayment(cardToSacrifice1, cardToSacrifice2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("discount color must match the purchased card's discount color",
        response.getBody());
    player.hand().reservedCards().clear();
    player.hand().purchasedCards().clear();

    cardToSacrifice1 =
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE);
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(cardToSacrifice1);
    player.hand().purchasedCards().add(cardToSacrifice2);
    payment = new CardPayment(cardToSacrifice1, cardToSacrifice2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true,
        new TradingPostTakeGem(GemColor.BLUE, GemColor.GOLD));
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.BLACK, 9);
    board.availableGems().clear();
    board.availableGems().put(GemColor.BLUE, 1);
    player.hand().gemDiscounts().put(GemColor.BLUE, 2);
    player.hand().incrementPrestigePoints(
        cardToSacrifice1.prestigePoints() + cardToSacrifice2.prestigePoints());
    player.hand().tradingPosts().put(TradingPostsEnum.BONUS_GEM_WITH_CARD, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    System.out.println(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(player.hand().reservedCards().isEmpty());
    assertTrue(player.hand().purchasedCards().contains(cardToPurchase));
    assertFalse(player.hand().purchasedCards().contains(cardToSacrifice1));
    assertFalse(player.hand().purchasedCards().contains(cardToSacrifice2));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.SINGLE.getValue(), player.hand().gemDiscounts()
        .getOrDefault(((SacrificeCard) cardToPurchase).discountColor(), 0));
    assertFalse(player.hand().gems().containsKey(GemColor.GOLD));
    assertEquals(1, player.hand().gems().getOrDefault(GemColor.BLUE, 0));
    assertFalse(board.availableGems().containsKey(GemColor.BLUE));
    assertEquals(1, board.availableGems().getOrDefault(GemColor.GOLD, 0));
    player.hand().purchasedCards().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
  }

  @Test
  public void testPurchaseCardWithSimpleGemPayment() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Set<Expansion> expansions = GameServiceName.getExpansions(GameServiceName.ALL);
    final Player player = new Player("test", expansions);
    GameBoard board =
        new GameBoard(expansions, Set.of(player, new Player("test2", expansions)), "x",
            null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);
    Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers =
        (Map<String, BroadcastContentManager<GameBoard>>) ReflectionTestUtils.getField(
            gameHandlerController,
            "gameSpecificBroadcastManagers");
    assertNotNull(gameSpecificBroadcastManagers);
    gameSpecificBroadcastManagers.put("x", new BroadcastContentManager<>(board));
    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    Card cardToPurchase = new SacrificeCard();
    player.hand().reservedCards().add(cardToPurchase);
    Payment payment = new GemPayment();
    PurchaseCardForm purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    ResponseEntity<String> response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot pay for a card with gems if it is a sacrifice card",
        response.getBody());
    player.hand().reservedCards().clear();

    cardToPurchase = new StandardCard();
    player.hand().reservedCards().add(cardToPurchase);
    payment = new GemPayment(null, -2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("numGoldGemCards cannot be less than 0", response.getBody());
    player.hand().reservedCards().clear();

    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(new GoldGemCard());
    payment = new GemPayment(null, 2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("do not have enough gold gem cards", response.getBody());
    player.hand().reservedCards().clear();

    player.hand().reservedCards().add(cardToPurchase);
    player.hand().purchasedCards().add(new GoldGemCard());
    final Gems chosenGems = new Gems();
    chosenGems.put(GemColor.BLUE, 1);
    payment = new GemPayment(chosenGems, 2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("not enough gems owned", response.getBody());
    player.hand().reservedCards().clear();
    player.hand().gems().clear();

    final Gems cardCost = new Gems();
    cardCost.put(GemColor.BLACK, 2);
    cardToPurchase =
        new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().gems().put(GemColor.BLUE, 2);
    player.hand().reservedCards().add(cardToPurchase);
    chosenGems.put(GemColor.BLUE, 2);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("gems used to pay don't match up to cost", response.getBody());
    player.hand().reservedCards().clear();
    player.hand().gems().clear();
    cardCost.clear();

    cardCost.put(GemColor.BLUE, 2);
    cardToPurchase =
        new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
    player.hand().reservedCards().add(cardToPurchase);
    player.hand().gems().put(GemColor.BLUE, 2);
    player.hand().reservedCards().add(cardToPurchase);
    chosenGems.put(GemColor.BLUE, 2);
    payment = new GemPayment(chosenGems, 1);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);

//    response =
//        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
//    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    assertEquals("cannot waste more than one gem when gold gem cards are used"
//                 + " and trading post 2 isn't acquired", response.getBody());
//    player.hand().reservedCards().clear();
//    chosenGems.clear();
//    player.hand().gems().clear();
//
//    cardCost.put(GemColor.BLUE, 3);
//    cardToPurchase =
//        new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
//    player.hand().reservedCards().add(cardToPurchase);
//    player.hand().gems().put(GemColor.BLUE, 2);
//    player.hand().reservedCards().add(cardToPurchase);
//    chosenGems.put(GemColor.BLUE, 2);
//    payment = new GemPayment(chosenGems, 0);
//    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);
//
//    response =
//        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
//    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    assertEquals("not enough gold gems used", response.getBody());
//    player.hand().reservedCards().clear();
//    chosenGems.clear();
//    player.hand().gems().clear();
//
//    cardCost.put(GemColor.BLUE, 3);
//    cardToPurchase =
//        new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
//    player.hand().reservedCards().add(cardToPurchase);
//    player.hand().gems().put(GemColor.BLUE, 2);
//    player.hand().gems().put(GemColor.GOLD, 2);
//    player.hand().reservedCards().add(cardToPurchase);
//    chosenGems.put(GemColor.BLUE, 2);
//    chosenGems.put(GemColor.GOLD, 2);
//    payment = new GemPayment(chosenGems, 0);
//    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);
//
//    response =
//        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
//    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    assertEquals("cannot waste any gold gems when trading post 2"
//                 + " isn't acquired and no gold gem cards are used", response.getBody());
//    player.hand().reservedCards().clear();
//    chosenGems.clear();
//    player.hand().gems().clear();
//
//    cardCost.put(GemColor.BLUE, 3);
//    cardToPurchase =
//        new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
//    player.hand().reservedCards().add(cardToPurchase);
//    player.hand().gems().put(GemColor.BLUE, 2);
//    player.hand().gems().put(GemColor.GOLD, 2);
//    player.hand().reservedCards().add(cardToPurchase);
//    chosenGems.put(GemColor.BLUE, 2);
//    chosenGems.put(GemColor.GOLD, 2);
//    player.hand().tradingPosts().put(TradingPostsEnum.DOUBLE_GOLD_GEMS, true);
//    payment = new GemPayment(chosenGems, 0);
//    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);
//
//    response =
//        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
//    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    assertEquals("cannot waste more than one gem when trading post 2"
//                 + " is acquired and no gold gem cards are used", response.getBody());
//    player.hand().reservedCards().clear();
//    chosenGems.clear();
//    player.hand().gems().clear();
//    player.hand().tradingPosts().put(TradingPostsEnum.DOUBLE_GOLD_GEMS, false);
//
//    cardCost.put(GemColor.BLUE, 3);
//    cardToPurchase =
//        new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
//    player.hand().reservedCards().add(cardToPurchase);
//    player.hand().gems().put(GemColor.BLUE, 2);
//    player.hand().gems().put(GemColor.GOLD, 1);
//    player.hand().reservedCards().add(cardToPurchase);
//    chosenGems.put(GemColor.BLUE, 2);
//    chosenGems.put(GemColor.GOLD, 1);
//    player.hand().tradingPosts().put(TradingPostsEnum.DOUBLE_GOLD_GEMS, true);
//    payment = new GemPayment(chosenGems, 1);
//    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);
//
//    response =
//        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
//    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    assertEquals("cannot waste more than three gems when gold gem cards are used"
//                 + " and trading post 2 is acquired", response.getBody());
    player.hand().reservedCards().clear();
    chosenGems.clear();
    player.hand().gems().clear();
    player.hand().tradingPosts().put(TradingPostsEnum.DOUBLE_GOLD_GEMS, false);

    cardCost.clear();
    cardCost.put(GemColor.BLUE, 2);
    cardCost.put(GemColor.RED, 1);
    cardCost.put(GemColor.BLACK, 3);
    cardToPurchase =
        new StandardCard(1, cardCost, CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE);
    player.hand().gems().put(GemColor.BLUE, 1);
    player.hand().reservedCards().add(cardToPurchase);
    chosenGems.put(GemColor.BLUE, 1);
    player.hand().gemDiscounts().put(GemColor.RED, 1);
    player.hand().gemDiscounts().put(GemColor.BLUE, 1);
    payment = new GemPayment(chosenGems, 2);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true);
    board.availableGems().clear();

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, player.hand().purchasedCards().size());
    assertTrue(player.hand().purchasedCards().contains(cardToPurchase));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertEquals(2,
        player.hand().gemDiscounts().get(((StandardCard) cardToPurchase).discountColor()));
    Gems expectedGameBoardGems = new Gems(board.availableGems());
    expectedGameBoardGems.put(GemColor.BLUE, 1);
    assertEquals(expectedGameBoardGems, board.availableGems());
    assertTrue(player.hand().gems().isEmpty());
    assertEquals(2, player.hand().gemDiscounts().get(GemColor.BLUE));
    player.hand().reservedCards().clear();
    chosenGems.clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    player.hand().gems().clear();
    board.nextTurn();

    cardCost.clear();
    cardToPurchase =
        new StandardCard(1, cardCost, CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE);
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.RED, 9);
    player.hand().reservedCards().add(cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, true,
        new TradingPostTakeGem(GemColor.BLUE, GemColor.GOLD));
    board.availableGems().clear();
    board.availableGems().put(GemColor.BLUE, 1);
    player.hand().tradingPosts().put(TradingPostsEnum.BONUS_GEM_WITH_CARD, true);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, player.hand().purchasedCards().size());
    assertTrue(player.hand().purchasedCards().contains(cardToPurchase));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertFalse(player.hand().gems().containsKey(GemColor.GOLD));
    assertEquals(1, player.hand().gems().getOrDefault(GemColor.BLUE, 0));
    assertFalse(board.availableGems().containsKey(GemColor.BLUE));
    assertEquals(1, board.availableGems().getOrDefault(GemColor.GOLD, 0));
    player.hand().reservedCards().clear();
    chosenGems.clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
  }

  @Test
  public void testPurchaseCardWithComplexGemPayment() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Set<Expansion> expansions = GameServiceName.getExpansions(GameServiceName.ALL);
    final Player player = new Player("test", expansions);
    GameBoard board =
        new GameBoard(expansions, Set.of(player, new Player("test2", expansions)), "x",
            null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);
    Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers =
        (Map<String, BroadcastContentManager<GameBoard>>) ReflectionTestUtils.getField(
            gameHandlerController,
            "gameSpecificBroadcastManagers");
    assertNotNull(gameSpecificBroadcastManagers);
    gameSpecificBroadcastManagers.put("x", new BroadcastContentManager<>(board));
    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");
    player.hand().gems().clear();
    board.availableGems().clear();

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    // Test double bonus card
    final Gems chosenGems = new Gems();
    final Gems cardCost = new Gems();
    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 1);
    cardCost.put(GemColor.BLACK, 3);
    cardCost.put(GemColor.BLUE, 1);
    Card cardToPurchase =
        new DoubleBonusCard(0, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.BLUE);
    player.hand().gems().put(GemColor.BLUE, 2);
    player.hand().gems().put(GemColor.GOLD, 2);
    chosenGems.put(GemColor.BLUE, 1);
    chosenGems.put(GemColor.GOLD, 2);
    player.hand().gemDiscounts().put(GemColor.WHITE, 3);
    player.hand().gemDiscounts().put(GemColor.RED, 1);
    player.hand().gemDiscounts().put(GemColor.BLACK, 1);
    addCardToDeck(board.cards(), cardToPurchase);
    Payment payment = new GemPayment(chosenGems, 0);
    PurchaseCardForm purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    ResponseEntity<String> response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, player.hand().purchasedCards().size());
    assertTrue(player.hand().purchasedCards().contains(cardToPurchase));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.DOUBLE.getValue(),
        player.hand().gemDiscounts().get(((DoubleBonusCard) cardToPurchase).discountColor()));
    Gems expectedGameBoardGems = new Gems(board.availableGems());
    expectedGameBoardGems.put(GemColor.BLUE, 1);
    expectedGameBoardGems.put(GemColor.GOLD, 2);
    assertEquals(expectedGameBoardGems, board.availableGems());
    assertEquals(1, player.hand().gems().getOrDefault(GemColor.BLUE, 0));
    assertEquals(Bonus.DOUBLE.getValue(), player.hand().gemDiscounts().get(GemColor.BLUE));
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();

    // Test Reserve noble card
    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToPurchase =
        new ReserveNobleCard(1, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.WHITE);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    final Gems nobleCost = new Gems();
    nobleCost.put(GemColor.RED, 100);
    board.availableNobles().add(new Noble(100, nobleCost));
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("noble to reserve cannot be null", response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToPurchase =
        new ReserveNobleCard(1, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.WHITE,
            new Noble(200, new Gems()));
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("noble to reserve is not available", response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToPurchase =
        new ReserveNobleCard(1, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.WHITE,
            new Noble(100, nobleCost));
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, player.hand().purchasedCards().size());
    assertTrue(player.hand().purchasedCards().contains(
        new ReserveNobleCard(1, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.WHITE)));
    assertTrue(player.hand().reservedNobles().contains(new Noble(100, nobleCost)));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.SINGLE.getValue(),
        player.hand().gemDiscounts().get(((ReserveNobleCard) cardToPurchase).discountColor()));
    expectedGameBoardGems = new Gems(board.availableGems());
    expectedGameBoardGems.put(GemColor.WHITE, 2);
    expectedGameBoardGems.put(GemColor.RED, 2);
    expectedGameBoardGems.put(GemColor.BLACK, 2);
    expectedGameBoardGems.put(GemColor.BLUE, 2);
    assertEquals(expectedGameBoardGems, board.availableGems());
    assertTrue(player.hand().gems().isEmpty());
    chosenGems.clear();
    cardCost.clear();
    player.hand().reservedNobles().clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.availableNobles().clear();

    // Test that when there are no nobles to reserve, you can still purchase a reserve noble card
    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToPurchase =
        new ReserveNobleCard(1, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.WHITE);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, player.hand().purchasedCards().size());
    assertTrue(player.hand().purchasedCards().contains(cardToPurchase));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.SINGLE.getValue(),
        player.hand().gemDiscounts().get(((ReserveNobleCard) cardToPurchase).discountColor()));
    expectedGameBoardGems = new Gems(board.availableGems());
    expectedGameBoardGems.put(GemColor.WHITE, 2);
    expectedGameBoardGems.put(GemColor.RED, 2);
    expectedGameBoardGems.put(GemColor.BLACK, 2);
    expectedGameBoardGems.put(GemColor.BLUE, 2);
    assertEquals(expectedGameBoardGems, board.availableGems());
    assertTrue(player.hand().gems().isEmpty());
    chosenGems.clear();
    cardCost.clear();
    player.hand().reservedNobles().clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.availableNobles().clear();

    // Test satchel card
    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToPurchase =
        new SatchelCard(1, cardCost, CardLevel.ONE, Expansion.ORIENT);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card to attach cannot be null", response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToPurchase =
        new SatchelCard(1, cardCost, CardLevel.THREE, Expansion.ORIENT, new StandardCard());
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("satchel card configured to the wrong level", response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToPurchase =
        new SatchelCard(1, cardCost, CardLevel.ONE, Expansion.ORIENT, new StandardCard(),
            new StandardCard());
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("level one satchel card does not allow you to take a free card",
        response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToPurchase =
        new SatchelCard(1, cardCost, CardLevel.ONE, Expansion.ORIENT, new StandardCard());
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot attach a satchel card to a card that you don't own",
        response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    Card cardToAttach = new GoldGemCard();
    cardToPurchase =
        new SatchelCard(1, cardCost, CardLevel.ONE, Expansion.ORIENT, cardToAttach);
    player.hand().purchasedCards().add(cardToAttach);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot attach a card that has no gem discount to a satchel card",
        response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToAttach =
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.WHITE);
    cardToPurchase =
        new SatchelCard(1, cardCost, CardLevel.ONE, Expansion.ORIENT, cardToAttach);
    player.hand().purchasedCards().add(cardToAttach);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, player.hand().purchasedCards().size());
    assertTrue(player.hand().purchasedCards().contains(cardToPurchase));
    assertFalse(player.hand().purchasedCards().contains(cardToAttach));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.SINGLE.getValue(),
        player.hand().gemDiscounts().get(((StandardCard) cardToAttach).discountColor()));
    expectedGameBoardGems = new Gems(board.availableGems());
    expectedGameBoardGems.put(GemColor.WHITE, 2);
    expectedGameBoardGems.put(GemColor.RED, 2);
    expectedGameBoardGems.put(GemColor.BLACK, 2);
    expectedGameBoardGems.put(GemColor.BLUE, 2);
    assertEquals(expectedGameBoardGems, board.availableGems());
    assertTrue(player.hand().gems().isEmpty());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.cards().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToAttach = new StandardCard();
    cardToPurchase =
        new SatchelCard(1, cardCost, CardLevel.TWO, Expansion.ORIENT, cardToAttach);
    player.hand().purchasedCards().add(cardToAttach);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(),
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE));
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card to take cannot be null",
        response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.cards().clear();

    // Show that it's fine to not take a free card if none are available
    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToAttach =
        new DoubleBonusCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT, GemColor.BLACK);
    cardToPurchase =
        new SatchelCard(1, cardCost, CardLevel.TWO, Expansion.ORIENT, cardToAttach);
    player.hand().purchasedCards().add(cardToAttach);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, player.hand().purchasedCards().size());
    assertTrue(player.hand().purchasedCards().contains(cardToPurchase));
    assertFalse(player.hand().purchasedCards().contains(cardToAttach));
    assertEquals(cardToPurchase.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.SINGLE.getValue(),
        player.hand().gemDiscounts().get(((DoubleBonusCard) cardToAttach).discountColor()));
    expectedGameBoardGems = new Gems(board.availableGems());
    expectedGameBoardGems.put(GemColor.WHITE, 2);
    expectedGameBoardGems.put(GemColor.RED, 2);
    expectedGameBoardGems.put(GemColor.BLACK, 2);
    expectedGameBoardGems.put(GemColor.BLUE, 2);
    assertEquals(expectedGameBoardGems, board.availableGems());
    assertTrue(player.hand().gems().isEmpty());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.cards().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    Card cardToTake =
        new DoubleBonusCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT, GemColor.BLACK);
    cardToAttach = new StandardCard();
    cardToPurchase =
        new SatchelCard(1, cardCost, CardLevel.TWO, Expansion.ORIENT, cardToAttach, cardToTake);
    player.hand().purchasedCards().add(cardToAttach);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(),
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE));
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("can only take a free level one card from a level two satchel card",
        response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.cards().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToTake =
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
    cardToAttach = new StandardCard();
    cardToPurchase =
        new SatchelCard(1, cardCost, CardLevel.TWO, Expansion.ORIENT, cardToAttach, cardToTake);
    player.hand().purchasedCards().add(cardToAttach);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(),
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLUE));
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("free card to take is not available", response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.cards().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToTake =
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
    cardToAttach =
        new DoubleBonusCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT, GemColor.BLUE);
    cardToPurchase =
        new SatchelCard(0, cardCost, CardLevel.TWO, Expansion.ORIENT, cardToAttach, cardToTake);
    player.hand().purchasedCards().add(cardToAttach);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToTake);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, player.hand().purchasedCards().size());
    assertTrue(player.hand().purchasedCards()
        .contains(new SatchelCard(0, cardCost, CardLevel.TWO, Expansion.ORIENT, cardToAttach)));
    assertTrue(player.hand().purchasedCards().contains(cardToTake));
    assertFalse(player.hand().purchasedCards().contains(cardToAttach));
    assertEquals(cardToTake.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.SINGLE.getValue(),
        player.hand().gemDiscounts().get(((DoubleBonusCard) cardToAttach).discountColor()));
    assertEquals(Bonus.SINGLE.getValue(),
        player.hand().gemDiscounts().get(((StandardCard) cardToTake).discountColor()));
    expectedGameBoardGems = new Gems(board.availableGems());
    expectedGameBoardGems.put(GemColor.WHITE, 2);
    expectedGameBoardGems.put(GemColor.RED, 2);
    expectedGameBoardGems.put(GemColor.BLACK, 2);
    expectedGameBoardGems.put(GemColor.BLUE, 2);
    assertEquals(expectedGameBoardGems, board.availableGems());
    assertTrue(player.hand().gems().isEmpty());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.cards().clear();

    // Test waterfall card
    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToPurchase =
        new WaterfallCard(0, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.RED, null);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(),
        new StandardCard(1, new Gems(), CardLevel.TWO, Expansion.STANDARD, GemColor.BLUE));
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card to take cannot be null", response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.cards().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToTake =
        new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
    cardToPurchase =
        new WaterfallCard(0, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.RED, cardToTake);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(),
        new StandardCard(1, new Gems(), CardLevel.TWO, Expansion.STANDARD, GemColor.BLUE));
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("must take a level two card for free", response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.cards().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToTake =
        new StandardCard(1, new Gems(), CardLevel.TWO, Expansion.STANDARD, GemColor.BLACK);
    cardToPurchase =
        new WaterfallCard(0, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.RED, cardToTake);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(),
        new StandardCard(1, new Gems(), CardLevel.TWO, Expansion.STANDARD, GemColor.BLUE));
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card to take is not available", response.getBody());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.cards().clear();

    cardCost.put(GemColor.WHITE, 2);
    cardCost.put(GemColor.RED, 2);
    cardCost.put(GemColor.BLACK, 2);
    cardCost.put(GemColor.BLUE, 2);
    cardToTake =
        new DoubleBonusCard(0, new Gems(), CardLevel.TWO, Expansion.ORIENT, GemColor.BLACK);
    cardToPurchase =
        new WaterfallCard(0, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.RED, cardToTake);
    player.hand().gems().put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.RED, 2);
    player.hand().gems().put(GemColor.BLACK, 2);
    player.hand().gems().put(GemColor.BLUE, 2);
    chosenGems.put(GemColor.WHITE, 2);
    chosenGems.put(GemColor.RED, 2);
    chosenGems.put(GemColor.BLACK, 2);
    chosenGems.put(GemColor.BLUE, 2);
    addCardToDeck(board.cards(), cardToTake);
    addCardToDeck(board.cards(), cardToPurchase);
    payment = new GemPayment(chosenGems, 0);
    purchaseCardForm = new PurchaseCardForm(cardToPurchase, payment, false);

    response =
        gameHandlerController.purchaseCard("", "token", gsonInstance.gson.toJson(purchaseCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, player.hand().purchasedCards().size());
    assertTrue(player.hand().purchasedCards()
        .contains(new WaterfallCard(0, cardCost, CardLevel.TWO, Expansion.ORIENT, GemColor.RED)));
    assertTrue(player.hand().purchasedCards().contains(cardToTake));
    assertEquals(cardToTake.prestigePoints(), player.hand().prestigePoints());
    assertEquals(Bonus.DOUBLE.getValue(),
        player.hand().gemDiscounts().get(((DoubleBonusCard) cardToTake).discountColor()));
    expectedGameBoardGems = new Gems(board.availableGems());
    expectedGameBoardGems.put(GemColor.WHITE, 2);
    expectedGameBoardGems.put(GemColor.RED, 2);
    expectedGameBoardGems.put(GemColor.BLACK, 2);
    expectedGameBoardGems.put(GemColor.BLUE, 2);
    assertEquals(expectedGameBoardGems, board.availableGems());
    assertTrue(player.hand().gems().isEmpty());
    chosenGems.clear();
    cardCost.clear();
    player.hand().purchasedCards().clear();
    player.hand().gems().clear();
    player.hand().gemDiscounts().clear();
    player.hand().setPrestigePoints(0);
    board.availableGems().clear();
    board.cards().clear();
  }

  private void addCardToDeck(Set<List<Card>> decks, Card card) {
    for (List<Card> cards : decks) {
      if (!cards.isEmpty()) {
        final Card firstCard = cards.get(0);
        if (firstCard.level() == card.level() && firstCard.expansion() == card.expansion()) {
          cards.add(0, card);
          return;
        }
      }
    }
    final List<Card> cards = new ArrayList<>();
    cards.add(card);
    decks.add(cards);
  }

  @Test
  public void testReserveCard() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Set<Expansion> expansions = GameServiceName.getExpansions(GameServiceName.ALL);
    final Player player = new Player("test", expansions);
    GameBoard board = new GameBoard(expansions, Set.of(player), "x", null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);
    Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers =
        (Map<String, BroadcastContentManager<GameBoard>>) ReflectionTestUtils.getField(
            gameHandlerController,
            "gameSpecificBroadcastManagers");
    assertNotNull(gameSpecificBroadcastManagers);
    gameSpecificBroadcastManagers.put("x", new BroadcastContentManager<>(board));

    ResponseEntity<String> response = gameHandlerController.reserveCard("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("invalid access token", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("x");

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.reserveCard("x", "token", null);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.reserveCard("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("player is not part of this game", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");

    Gems cardCost = new Gems();
    cardCost.put(GemColor.GREEN, 2);
    cardCost.put(GemColor.WHITE, 2);
    Card card = new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
    addCardToDeck(board.cards(), card);
    ReserveCardForm reserveCardForm = new ReserveCardForm(card, GemColor.WHITE, false);

    player.hand().gems().clear();

    response =
        gameHandlerController.reserveCard("", "token", gsonInstance.gson.toJson(reserveCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot get rid of a gem if you have less than 10 gems", response.getBody());

    player.hand().gems().put(GemColor.BLUE, 10);

    response =
        gameHandlerController.reserveCard("", "token", gsonInstance.gson.toJson(reserveCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot get rid of a gem that you do not have", response.getBody());

    player.hand().gems().put(GemColor.WHITE, 1);
    player.hand().gems().put(GemColor.BLUE, 9);
    board.availableGems().clear();

    response =
        gameHandlerController.reserveCard("", "token", gsonInstance.gson.toJson(reserveCardForm));
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

    card = new StandardCard(0, new Gems(), CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
    reserveCardForm = new ReserveCardForm(card, GemColor.WHITE, false);

    response =
        gameHandlerController.reserveCard("", "token", gsonInstance.gson.toJson(reserveCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card chosen for reservation is not valid", response.getBody());

    card = new StandardCard(0, cardCost, CardLevel.ONE, Expansion.STANDARD, GemColor.BLACK);
    reserveCardForm = new ReserveCardForm(card, GemColor.WHITE, false);

    response =
        gameHandlerController.reserveCard("", "token", gsonInstance.gson.toJson(reserveCardForm));
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

    board = new GameBoard(new HashSet<>(), Set.of(player), "x", null);
    gameManager.put("", board);
    board.availableGems().clear();
    board.availableGems().put(GemColor.GOLD, 2);
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.BLUE, 8);
    reserveCardForm = new ReserveCardForm(card, null, false);
    addCardToDeck(board.cards(), card);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.reserveCard("", "token", gsonInstance.gson.toJson(reserveCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, player.hand().gems().get(GemColor.GOLD));
    assertEquals(1, board.availableGems().get(GemColor.GOLD));
    assertEquals(1, player.hand().reservedCards().size());
    assertTrue(player.hand().reservedCards().remove(card));
    assertTrue(player.hand().reservedCards().isEmpty());

    board = new GameBoard(new HashSet<>(), Set.of(player), "x", null);
    gameManager.put("", board);
    board.availableGems().clear();
    board.availableGems().put(GemColor.GOLD, 2);
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.BLUE, 8);
    reserveCardForm = new ReserveCardForm(null, null, false);

    if (!board.isPlayerTurn("test")) {
      response =
          gameHandlerController.reserveCard("", "token", gsonInstance.gson.toJson(reserveCardForm));
      assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
      assertEquals("cannot take an action outside of your turn", response.getBody());
      board.nextTurn();
    }
    response =
        gameHandlerController.reserveCard("", "token", gsonInstance.gson.toJson(reserveCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("card cannot be null", response.getBody());

    board = new GameBoard(new HashSet<>(), Set.of(player), "x", null);
    ReflectionTestUtils.setField(board, "cards", new HashSet<>());
    gameManager.put("", board);
    board.availableGems().clear();
    board.availableGems().put(GemColor.GOLD, 2);
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.BLUE, 8);
    reserveCardForm = new ReserveCardForm(card, null, true);

    response =
        gameHandlerController.reserveCard("", "token", gsonInstance.gson.toJson(reserveCardForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("not enough cards in the deck to reserve a face down card", response.getBody());

    board = new GameBoard(new HashSet<>(), Set.of(player), "x", null);
    gameManager.put("", board);
    board.availableGems().clear();
    board.availableGems().put(GemColor.GOLD, 2);
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 1);
    player.hand().gems().put(GemColor.BLUE, 8);
    card = new StandardCard(99, new Gems(), CardLevel.TWO, Expansion.STANDARD, GemColor.BLUE);
    reserveCardForm = new ReserveCardForm(card, null, true);

    response =
        gameHandlerController.reserveCard("", "token", gsonInstance.gson.toJson(reserveCardForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, player.hand().gems().get(GemColor.GOLD));
    assertEquals(1, board.availableGems().get(GemColor.GOLD));
    assertFalse(player.hand().reservedCards().remove(card));
    assertEquals(1, player.hand().reservedCards().size());
  }

  @Test
  public void testTakeGems() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Set<Expansion> expansions = GameServiceName.getExpansions(GameServiceName.ALL);
    final Player player = new Player("test", expansions);
    GameBoard board =
        new GameBoard(expansions, Set.of(player, new Player("test2", expansions)), "x",
            null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);
    Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers =
        (Map<String, BroadcastContentManager<GameBoard>>) ReflectionTestUtils.getField(
            gameHandlerController,
            "gameSpecificBroadcastManagers");
    assertNotNull(gameSpecificBroadcastManagers);
    gameSpecificBroadcastManagers.put("x", new BroadcastContentManager<>(board));

    ResponseEntity<String> response = gameHandlerController.takeGems("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("invalid access token", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("x");

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("x", "token", null);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.takeGems("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("player is not part of this game", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");

    TakeGemsForm takeGemsForm = new TakeGemsForm(null, null);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("gems to take cannot be null", response.getBody());

    final Gems gemsToTake = new Gems();
    gemsToTake.put(GemColor.GOLD, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take gold gems", response.getBody());

    gemsToTake.clear();
    gemsToTake.put(GemColor.BLACK, -1);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("gems to take cannot be <= 0", response.getBody());

    gemsToTake.clear();
    gemsToTake.put(GemColor.WHITE, 4);
    final Gems gemsToRemove = new Gems();
    takeGemsForm = new TakeGemsForm(gemsToTake, gemsToRemove);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take more than 3 gems", response.getBody());

    gemsToTake.put(GemColor.WHITE, 2);
    board.availableGems().clear();

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("not enough gems in the bank", response.getBody());

    gemsToTake.put(GemColor.WHITE, 2);
    board.availableGems().put(GemColor.WHITE, 5);
    player.hand().gems().clear();
    player.hand().gems().put(GemColor.GOLD, 10);
    gemsToRemove.put(GemColor.BLUE, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, gemsToRemove);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot remove gems that you do not own", response.getBody());

    takeGemsForm = new TakeGemsForm(gemsToTake, null);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("can only have a maximum of 10 gems", response.getBody());

    gemsToRemove.clear();
    gemsToRemove.put(GemColor.WHITE, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, gemsToRemove);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("can only have a maximum of 10 gems", response.getBody());

    player.hand().gems().put(GemColor.GOLD, 6);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot remove gems if not necessary", response.getBody());

    gemsToRemove.put(GemColor.GOLD, 2);
    player.hand().gems().put(GemColor.GOLD, 10);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot remove gems and be left with less than 10", response.getBody());

    player.hand().gems().put(GemColor.GOLD, 7);
    gemsToTake.put(GemColor.WHITE, 3);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take 3 gems of one color", response.getBody());

    gemsToTake.put(GemColor.WHITE, 2);
    board.availableGems().put(GemColor.WHITE, 3);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(
        "cannot take 2 same color gems, there are less than 4 gems of that color in the bank",
        response.getBody());

    gemsToTake.put(GemColor.WHITE, 1);
    board.availableGems().put(GemColor.WHITE, 4);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take 1 gem if it is possible to take more", response.getBody());

    board.availableGems().put(GemColor.WHITE, 3);
    board.availableGems().put(GemColor.BLUE, 3);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take 1 gem if it is possible to take more", response.getBody());

    gemsToTake.put(GemColor.BLUE, 2);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot take more than one gem of each gem", response.getBody());

    gemsToTake.put(GemColor.BLUE, 1);
    board.availableGems().put(GemColor.BLACK, 3);

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(
        "cannot take 1 gem of each for 2 gem colors if it is possible to take from 3 colors",
        response.getBody());

    // 1 gem of each for two different colors, when there are only 2 colors available
    gemsToTake.put(GemColor.BLUE, 1);
    board.availableGems().remove(GemColor.BLACK);
    Gems previousOwnedGems = new Gems(player.hand().gems());
    Gems previousBank = new Gems(board.availableGems());

    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
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

    if (!board.isPlayerTurn("test")) {
      response =
          gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
      assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
      assertEquals("cannot take an action outside of your turn", response.getBody());
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
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

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
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

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
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

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
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

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
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

    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToRemove.clear();
    gemsToTake.put(GemColor.WHITE, 2);
    player.hand().gems().put(GemColor.GOLD, 2);
    board.availableGems().put(GemColor.WHITE, 4);
    board.availableGems().put(GemColor.RED, 1);
    board.availableGems().put(GemColor.GREEN, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);
    player.hand().tradingPosts().put(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, true);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("must take an extra gem of another color"
                 + " if trading post 2 is acquired and it is possible", response.getBody());

    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToRemove.clear();
    gemsToTake.put(GemColor.WHITE, 1);
    gemsToTake.put(GemColor.RED, 1);
    player.hand().gems().put(GemColor.GOLD, 2);
    board.availableGems().put(GemColor.WHITE, 4);
    board.availableGems().put(GemColor.RED, 2);
    board.availableGems().put(GemColor.GREEN, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);
    player.hand().tradingPosts().put(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, true);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("must take more than one gem of a gem since you have trading post power 2",
        response.getBody());

    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToRemove.clear();
    gemsToTake.put(GemColor.WHITE, 1);
    gemsToTake.put(GemColor.RED, 1);
    player.hand().gems().put(GemColor.GOLD, 2);
    board.availableGems().put(GemColor.WHITE, 3);
    board.availableGems().put(GemColor.RED, 2);
    board.availableGems().put(GemColor.GREEN, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);
    player.hand().tradingPosts().put(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, true);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(
        "cannot take 1 gem of each for 2 gem colors if it is possible to take from 3 colors",
        response.getBody());

    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToRemove.clear();
    gemsToTake.put(GemColor.WHITE, 2);
    gemsToTake.put(GemColor.RED, 1);
    player.hand().gems().put(GemColor.GOLD, 2);
    board.availableGems().put(GemColor.WHITE, 3);
    board.availableGems().put(GemColor.RED, 2);
    board.availableGems().put(GemColor.GREEN, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);
    player.hand().tradingPosts().put(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, true);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(
        "cannot take 2 same color gems, there are less than 4 gems of that color in the bank",
        response.getBody());

    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToRemove.clear();
    gemsToTake.put(GemColor.WHITE, 2);
    gemsToTake.put(GemColor.RED, 1);
    player.hand().gems().put(GemColor.GOLD, 2);
    board.availableGems().put(GemColor.WHITE, 4);
    board.availableGems().put(GemColor.RED, 1);
    board.availableGems().put(GemColor.GREEN, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);
    player.hand().tradingPosts().put(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, true);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());

    // It is still okay to take 3 gems as normal (one gem of each color)
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
    takeGemsForm = new TakeGemsForm(gemsToTake, gemsToRemove);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
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

    // It is still okay to take 2 gems (one gem of each color) when there aren't more to take
    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToRemove.clear();
    gemsToTake.put(GemColor.WHITE, 1);
    gemsToTake.put(GemColor.RED, 1);
    board.availableGems().put(GemColor.WHITE, 3);
    board.availableGems().put(GemColor.RED, 1);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());

    // It is still okay to take 2 gems of one color when there isn't an extra color to take
    gemsToTake.clear();
    player.hand().gems().clear();
    board.availableGems().clear();
    gemsToRemove.clear();
    gemsToTake.put(GemColor.WHITE, 2);
    board.availableGems().put(GemColor.WHITE, 4);
    takeGemsForm = new TakeGemsForm(gemsToTake, null);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response = gameHandlerController.takeGems("", "token", gsonInstance.gson.toJson(takeGemsForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testClaimNoble() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Set<Expansion> expansions = GameServiceName.getExpansions(GameServiceName.ALL);
    final Player player = new Player("test", expansions);
    GameBoard board =
        new GameBoard(expansions, Set.of(player, new Player("test2", expansions)), "x",
            null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);
    Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers =
        (Map<String, BroadcastContentManager<GameBoard>>) ReflectionTestUtils.getField(
            gameHandlerController,
            "gameSpecificBroadcastManagers");
    assertNotNull(gameSpecificBroadcastManagers);
    gameSpecificBroadcastManagers.put("x", new BroadcastContentManager<>(board));

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

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.claimNoble("", "token", gsonInstance.gson.toJson(claimNobleForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("noble to claim cannot be null", response.getBody());

    final Gems costOfNoble = new Gems();
    costOfNoble.put(GemColor.RED, 2);
    costOfNoble.put(GemColor.BLACK, 1);
    Noble nobleToClaim = new Noble(2, costOfNoble);
    player.hand().gemDiscounts().clear();
    claimNobleForm = new ClaimNobleForm(nobleToClaim);

    response =
        gameHandlerController.claimNoble("", "token", gsonInstance.gson.toJson(claimNobleForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("do not have enough gem discounts to claim noble", response.getBody());

    player.hand().gemDiscounts().put(GemColor.BLUE, 1);
    player.hand().gemDiscounts().put(GemColor.BLACK, 1);
    player.hand().gemDiscounts().put(GemColor.RED, 3);
    claimNobleForm = new ClaimNobleForm(nobleToClaim);

    response =
        gameHandlerController.claimNoble("", "token", gsonInstance.gson.toJson(claimNobleForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("noble to claim is not available", response.getBody());

    player.hand().reservedNobles().add(nobleToClaim);
    int prevPrestigePoints = player.hand().prestigePoints();

    response =
        gameHandlerController.claimNoble("", "token", gsonInstance.gson.toJson(claimNobleForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(player.hand().reservedNobles().isEmpty());
    assertEquals(Set.of(nobleToClaim), player.hand().visitedNobles());
    assertEquals(prevPrestigePoints + nobleToClaim.prestigePoints(),
        player.hand().prestigePoints());

    player.hand().visitedNobles().remove(nobleToClaim);
    assertTrue(player.hand().visitedNobles().isEmpty());

    nobleToClaim = new Noble(3, costOfNoble);
    claimNobleForm = new ClaimNobleForm(nobleToClaim);
    board.availableNobles().add(nobleToClaim);
    prevPrestigePoints = player.hand().prestigePoints();

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.claimNoble("", "token", gsonInstance.gson.toJson(claimNobleForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(board.availableNobles().contains(nobleToClaim));
    assertEquals(Set.of(nobleToClaim), player.hand().visitedNobles());
    assertEquals(prevPrestigePoints + nobleToClaim.prestigePoints(),
        player.hand().prestigePoints());

    player.hand().visitedNobles().remove(nobleToClaim);
    assertTrue(player.hand().visitedNobles().isEmpty());
  }

  @Test
  public void testClaimCity() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Set<Expansion> expansions = GameServiceName.getExpansions(GameServiceName.ALL);
    final Player player = new Player("test", expansions);
    GameBoard board =
        new GameBoard(expansions, Set.of(player, new Player("test2", expansions)), "x",
            null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);
    Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers =
        (Map<String, BroadcastContentManager<GameBoard>>) ReflectionTestUtils.getField(
            gameHandlerController,
            "gameSpecificBroadcastManagers");
    assertNotNull(gameSpecificBroadcastManagers);
    gameSpecificBroadcastManagers.put("x", new BroadcastContentManager<>(board));

    ResponseEntity<String> response = gameHandlerController.claimCity("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("invalid access token", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("x");

    response = gameHandlerController.claimCity("x", "token", null);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    response = gameHandlerController.claimCity("", "token", null);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("player is not part of this game", response.getBody());

    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");

    ClaimCityForm claimCityForm = new ClaimCityForm(null);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.claimCity("", "token", gsonInstance.gson.toJson(claimCityForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("city to claim cannot be null", response.getBody());

    final Gems gemDiscounts = new Gems();
    gemDiscounts.put(GemColor.RED, 2);
    gemDiscounts.put(GemColor.BLACK, 1);
    City cityToClaim = new City(3, gemDiscounts);
    player.hand().gemDiscounts().clear();
    claimCityForm = new ClaimCityForm(cityToClaim);

    response =
        gameHandlerController.claimCity("", "token", gsonInstance.gson.toJson(claimCityForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("city to claim is not available", response.getBody());

    player.hand().gemDiscounts().put(GemColor.BLUE, 1);
    player.hand().gemDiscounts().put(GemColor.BLACK, 1);
    player.hand().gemDiscounts().put(GemColor.RED, 3);
    claimCityForm = new ClaimCityForm(cityToClaim);
    board.availableCities().add(cityToClaim);

    response =
        gameHandlerController.claimCity("", "token", gsonInstance.gson.toJson(claimCityForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("player does not qualify to claim this city", response.getBody());

    player.hand().setCity(cityToClaim);

    response =
        gameHandlerController.claimCity("", "token", gsonInstance.gson.toJson(claimCityForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("cannot own two cities", response.getBody());

    player.hand().setCity(null);
    player.hand().setPrestigePoints(3);

    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.claimCity("", "token", gsonInstance.gson.toJson(claimCityForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(board.availableCities().contains(cityToClaim));
    assertEquals(cityToClaim, player.hand().city());
    assertFalse(board.isPlayerTurn("test"));
    assertTrue(board.isLastRound());

    player.hand().gemDiscounts().clear();
    player.hand().gemDiscounts().put(GemColor.RED, 3);
    gemDiscounts.clear();
    gemDiscounts.put(GemColor.RED, 3);
    cityToClaim = new City(3, gemDiscounts);
    claimCityForm = new ClaimCityForm(cityToClaim);
    board.availableCities().add(cityToClaim);
    board.nextTurn();
    if (board.isLastRound() && !board.isGameOver()) {
      board.nextTurn();
      Mockito.when(lobbyService.getUsername("token")).thenReturn("test2");
    }
    player.hand().setCity(null);
    response =
        gameHandlerController.claimCity("", "token", gsonInstance.gson.toJson(claimCityForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("game is over", response.getBody());
    assertTrue(board.availableCities().contains(cityToClaim));
  }

  @Test
  public void testClaimCityWithGold() {
    final Map<String, GameBoard> gameManager = new HashMap<>();
    final Set<Expansion> expansions = GameServiceName.getExpansions(GameServiceName.ALL);
    final Player player = new Player("test", expansions);
    GameBoard board =
        new GameBoard(expansions, Set.of(player, new Player("test2", expansions)), "x",
            null);
    gameManager.put("", board);
    ReflectionTestUtils.setField(gameHandlerController, "gameManager", gameManager);
    Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers =
        (Map<String, BroadcastContentManager<GameBoard>>) ReflectionTestUtils.getField(
            gameHandlerController,
            "gameSpecificBroadcastManagers");
    assertNotNull(gameSpecificBroadcastManagers);
    gameSpecificBroadcastManagers.put("x", new BroadcastContentManager<>(board));

    Mockito.when(lobbyService.getUsername("token")).thenReturn("test");

    player.hand().setPrestigePoints(3);
    player.hand().gemDiscounts().clear();
    player.hand().gemDiscounts().put(GemColor.BLACK, 1);
    player.hand().gemDiscounts().put(GemColor.RED, 3);
    player.hand().setCity(null);

    final Gems gemDiscounts = new Gems();
    gemDiscounts.put(GemColor.RED, 3);
    gemDiscounts.put(GemColor.GOLD, 3);
    City cityToClaim = new City(3, gemDiscounts);
    ClaimCityForm claimCityForm = new ClaimCityForm(cityToClaim);
    board.availableCities().add(cityToClaim);
    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }

    ResponseEntity<String> response =
        gameHandlerController.claimCity("", "token", gsonInstance.gson.toJson(claimCityForm));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("player does not qualify to claim this city", response.getBody());
    assertTrue(board.availableCities().contains(cityToClaim));

    gemDiscounts.clear();
    gemDiscounts.put(GemColor.RED, 3);
    gemDiscounts.put(GemColor.GOLD, 1);
    cityToClaim = new City(3, gemDiscounts);
    claimCityForm = new ClaimCityForm(cityToClaim);
    board.availableCities().add(cityToClaim);
    if (!board.isPlayerTurn("test")) {
      board.nextTurn();
    }
    response =
        gameHandlerController.claimCity("", "token", gsonInstance.gson.toJson(claimCityForm));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(board.availableCities().contains(cityToClaim));
    assertEquals(cityToClaim, player.hand().city());
  }
}
