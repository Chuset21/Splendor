package hexanome.fourteen.server.control;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import hexanome.fourteen.server.Mapper;
import hexanome.fourteen.server.control.form.ClaimNobleForm;
import hexanome.fourteen.server.control.form.LaunchGameForm;
import hexanome.fourteen.server.control.form.PurchaseCardForm;
import hexanome.fourteen.server.control.form.ReserveCardForm;
import hexanome.fourteen.server.control.form.SaveGameForm;
import hexanome.fourteen.server.control.form.TakeGemsForm;
import hexanome.fourteen.server.control.form.payment.CardPayment;
import hexanome.fourteen.server.control.form.payment.GemPayment;
import hexanome.fourteen.server.control.form.payment.Payment;
import hexanome.fourteen.server.model.User;
import hexanome.fourteen.server.model.board.GameBoard;
import hexanome.fourteen.server.model.board.GameBoardHelper;
import hexanome.fourteen.server.model.board.Hand;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Controller to handle lobby service callbacks.
 */
@RestController
@RequestMapping("/api/games/")
public class GameHandlerController {

  private final long longPollTimeout;
  private final LobbyServiceCaller lobbyService;
  private final Map<String, GameBoard> gameManager;
  private final Map<String, BroadcastContentManager<GameBoard>> gameSpecificBroadcastManagers;
  private final Mapper<User, Player> userPlayerMapper;
  private final GsonInstance gsonInstance;
  private final SavedGamesService saveGameManager;
  private final ServerService serverService;

  /**
   * Constructor.
   *
   * @param lobbyService     Lobby service
   * @param userPlayerMapper The User to Player Mapper
   * @param gsonInstance     The GSON we will be using
   */
  public GameHandlerController(@Value("${long.poll.timeout}") long longPollTimeout,
                               @Autowired LobbyServiceCaller lobbyService,
                               @Autowired Mapper<User, Player> userPlayerMapper,
                               @Autowired GsonInstance gsonInstance,
                               @Autowired SavedGamesService saveGameManager,
                               @Autowired ServerService serverService) {
    this.longPollTimeout = longPollTimeout;
    this.lobbyService = lobbyService;
    this.userPlayerMapper = userPlayerMapper;
    this.gsonInstance = gsonInstance;
    this.saveGameManager = saveGameManager;
    this.serverService = serverService;
    gameManager = new HashMap<>();
    gameSpecificBroadcastManagers = new LinkedHashMap<>();
  }

  @GetMapping()
  public ResponseEntity<String> testApi() {
    return ResponseEntity.status(HttpStatus.OK).body("game service running");
  }

  /**
   * Create a game given the specified parameters.
   *
   * @param gameid            The game id for the created game
   * @param rawLaunchGameForm The information needed to create the game
   * @return The full response
   */
  @PutMapping(value = "{gameid}", consumes = MediaType.ALL_VALUE)
  public ResponseEntity<String> launchGame(@PathVariable String gameid,
                                           @RequestBody String rawLaunchGameForm) {
    final LaunchGameForm launchGameForm =
        gsonInstance.gson.fromJson(rawLaunchGameForm, LaunchGameForm.class);
    GameBoard game = null;
    if (launchGameForm.saveGame() != null && !launchGameForm.saveGame().isBlank()) {
      game = saveGameManager.getGame(launchGameForm.saveGame());
    }
    if (game == null) {
      game = createGame(gameid, launchGameForm);
    } else {
      game.setGameid(gameid);
    }
    gameManager.put(gameid, game);
    gameSpecificBroadcastManagers.put(gameid, new BroadcastContentManager<>(game));
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  private GameBoard createGame(String gameid, LaunchGameForm launchGameForm) {
    Set<Expansion> expansions =
        GameServiceName.getExpansions(GameServiceName.valueOf(launchGameForm.gameType()));
    Set<Player> players = Arrays.stream(launchGameForm.players()).map(userPlayerMapper::map)
        .collect(Collectors.toSet());
    String creator = launchGameForm.creator();

    return new GameBoard(expansions, players, gameid, creator);
  }

  /**
   * Remove a game with a matching game ID.
   * Only admins can delete a game.
   *
   * @param gameid game ID to remove
   * @return The full response
   */
  @DeleteMapping("{gameid}")
  public ResponseEntity<String> deleteGame(@PathVariable String gameid) {
    if (!removeGame(gameid)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    }
    gameSpecificBroadcastManagers.get(gameid).terminate();
    gameSpecificBroadcastManagers.remove(gameid);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  /**
   * Remove a game with the matching game ID.
   *
   * @param gameid game ID to remove
   * @return true if the game was successfully removed, false otherwise.
   */
  private boolean removeGame(String gameid) {
    return gameManager.remove(gameid) != null;
  }

  private GameBoard getGame(String gameid) {
    return gameManager.get(gameid);
  }

  private String getUsername(String accessToken) {
    return lobbyService.getUsername(accessToken);
  }

  /**
   * Get a game board.
   *
   * @param gameid      The game id corresponding to the game.
   * @param accessToken The access token belonging to the player getting the game.
   * @return The full response
   */
  @GetMapping(value = "{gameid}", produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> retrieveGame(@PathVariable String gameid,
                                                             @RequestParam("access_token")
                                                             String accessToken,
                                                             @RequestParam(required = false)
                                                             String hash) {
    final DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();
    final String username = getUsername(accessToken);
    if (username == null) {
      result.setResult(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token"));
      return result;
    }

    final GameBoard gameBoard = getGame(gameid);
    if (gameBoard == null) {
      result.setResult(ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found"));
      return result;
    } else if (GameBoardHelper.getHand(gameBoard.players(), username) == null) {
      result.setResult(
          ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("player is not part of this game"));
      return result;
    } else {
      // No hash provided at all -> return a synced update.
      // We achieve this by setting a hash that clearly differs from any valid hash.
      if (hash == null) {
        hash = "-";
      }

      // Hash was provided, but is empty -> return an asynchronous update,
      // as soon as something has changed
      if (hash.isBlank()) {
        return ResponseGenerator.getAsyncUpdate(longPollTimeout,
            gameSpecificBroadcastManagers.get(gameid));
      }

      // A hash was provided,
      // or we want to provoke a hash mismatch because no hash (not even an empty hash) was provided
      return ResponseGenerator.getHashBasedUpdate(longPollTimeout,
          gameSpecificBroadcastManagers.get(gameid), hash);
    }
  }

  /**
   * Save a game.
   *
   * @param gameid      The game id corresponding to the game.
   * @param accessToken The access token belonging to the player getting the game.
   * @return The response.
   */
  @PostMapping(value = "{gameid}", produces = "application/json; charset=utf-8")
  public ResponseEntity<String> saveGame(@PathVariable String gameid,
                                         @RequestParam("access_token") String accessToken) {
    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(gameid);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    } else if (GameBoardHelper.getHand(gameBoard.players(), username) == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("player is not part of this game");
    } else if (gameBoard.isGameOver()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("game is over");
    } else {
      if (!serverService.refreshToken()) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("token could not be refreshed");
      }
      String saveGameid = saveGameManager.putGame(gameBoard);
      lobbyService.saveGame(serverService.accessToken,
          new SaveGameForm(GameServiceName.getGameServiceName(gameBoard.expansions()).name(),
              gameBoard.players().stream().map(Player::uid).toList(), saveGameid));
      return ResponseEntity.status(HttpStatus.OK).body(saveGameid);
    }
  }

  /**
   * Purchase a given card.
   *
   * @param gameid             The game id corresponding to the game.
   * @param accessToken        The access token belonging to the player trying to purchase the card.
   * @param purchaseCardString The purchase card json string containing the form.
   * @return The response.
   */
  @PutMapping(value = "{gameid}/card/purchase", consumes = MediaType.ALL_VALUE)
  public ResponseEntity<String> purchaseCard(@PathVariable String gameid,
                                             @RequestParam("access_token") String accessToken,
                                             @RequestBody String purchaseCardString) {
    final PurchaseCardForm purchaseCardForm =
        gsonInstance.gson.fromJson(purchaseCardString, PurchaseCardForm.class);

    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(gameid);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    } else if (gameBoard.isGameOver()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("game is over");
    }

    final Hand hand = GameBoardHelper.getHand(gameBoard.players(), username);
    if (hand == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("player is not part of this game");
    }

    final Card card = purchaseCardForm.card();
    if (card == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("card cannot be null");
    } else if (purchaseCardForm.isReserved() && !hand.reservedCards().contains(card)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("card is not reserved");
    }

    final Payment payment = purchaseCardForm.payment();
    if (payment == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("payment cannot be null");
    }

    final Set<List<Card>> decks = gameBoard.cards();
    if (!GameBoardHelper.isCardFaceUpOnBoard(decks, card) && !purchaseCardForm.isReserved()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("card chosen for purchase is not valid");
    }

    return handlePayment(gameBoard, hand, purchaseCardForm);
  }

  private ResponseEntity<String> handlePayment(GameBoard gameBoard, Hand hand,
                                               PurchaseCardForm purchaseCardForm) {
    final Payment payment = purchaseCardForm.payment();
    return payment instanceof CardPayment c
        ? handlePayment(gameBoard, hand, purchaseCardForm.card(), c,
        purchaseCardForm.isReserved())
        : handlePayment(gameBoard, hand, purchaseCardForm.card(), (GemPayment) payment,
        purchaseCardForm.isReserved());
  }

  private ResponseEntity<String> handlePayment(GameBoard gameBoard, Hand hand, Card card,
                                               CardPayment payment, boolean reserved) {
    if (!(card instanceof SacrificeCard cardToPurchase)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("can only pay for a card with cards if it is a sacrifice card");
    }

    final Card cardToSacrifice1 = payment.getCardToSacrifice1();
    final Card cardToSacrifice2 = payment.getCardToSacrifice2();
    if (cardToSacrifice1 == null && cardToSacrifice2 == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("both cards to sacrifice cannot be null");
    }

    if (cardToSacrifice1 == null || cardToSacrifice2 == null) {
      final Card cardToSacrifice = cardToSacrifice1 == null ? cardToSacrifice2 : cardToSacrifice1;
      if (!hand.purchasedCards().contains(cardToSacrifice)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot sacrifice a card that you do not own");
      }

      if (!(cardToSacrifice instanceof DoubleBonusCard)
          && !(cardToSacrifice instanceof SatchelCard)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                "must sacrifice a double bonus card or a satchel card containing another card if "
                + "you're only sacrificing one card");
      }

      if (cardToSacrifice instanceof DoubleBonusCard doubleBonusCard) {
        if (hand.purchasedCards().stream()
            .anyMatch(e -> e instanceof SatchelCard satchel
                           && GameBoardHelper.matchesCardDiscountColor(cardToPurchase,
                satchel.cardToAttach()))) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("must sacrifice a satchel card if possible");
        }

        if (doubleBonusCard.discountColor() != cardToPurchase.discountColor()) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("sacrifice card discount color must equal purchased card discount color");
        }

        hand.gemDiscounts().computeIfPresent(doubleBonusCard.discountColor(),
            (k, v) -> v == Bonus.DOUBLE.getValue() ? null : v - Bonus.DOUBLE.getValue());
      } else {
        final SatchelCard satchelCard = (SatchelCard) cardToSacrifice;
        if (!GameBoardHelper.matchesCardDiscountColor(cardToPurchase, satchelCard.cardToAttach())) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("discount color must match the purchased card's discount color");
        }

        // Decrement gem discounts
        if (Objects.requireNonNull(satchelCard.cardToAttach()) instanceof ReserveNobleCard c) {
          hand.gemDiscounts().computeIfPresent(c.discountColor(),
              (k, v) -> v == Bonus.SINGLE.getValue() + Bonus.SINGLE.getValue() ? null :
                  v - Bonus.SINGLE.getValue() - Bonus.SINGLE.getValue());
        } else if (satchelCard.cardToAttach() instanceof DoubleBonusCard c) {
          hand.gemDiscounts().computeIfPresent(c.discountColor(),
              (k, v) -> v == Bonus.DOUBLE.getValue() + Bonus.SINGLE.getValue() ? null :
                  v - Bonus.DOUBLE.getValue() - Bonus.SINGLE.getValue());
        } else if (satchelCard.cardToAttach() instanceof SacrificeCard c) {
          hand.gemDiscounts().computeIfPresent(c.discountColor(),
              (k, v) -> v == Bonus.SINGLE.getValue() + Bonus.SINGLE.getValue() ? null :
                  v - Bonus.SINGLE.getValue() - Bonus.SINGLE.getValue());
        } else if (satchelCard.cardToAttach() instanceof StandardCard c) {
          hand.gemDiscounts().computeIfPresent(c.discountColor(),
              (k, v) -> v == Bonus.SINGLE.getValue() + Bonus.SINGLE.getValue() ? null :
                  v - Bonus.SINGLE.getValue() - Bonus.SINGLE.getValue());
        } else if (satchelCard.cardToAttach() instanceof WaterfallCard c) {
          hand.gemDiscounts().computeIfPresent(c.discountColor(),
              (k, v) -> v == Bonus.SINGLE.getValue() + Bonus.SINGLE.getValue() ? null :
                  v - Bonus.SINGLE.getValue() - Bonus.SINGLE.getValue());
        } else {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("failed to decrement prestige points");
        }
        hand.decrementPrestigePoints(satchelCard.cardToAttach().prestigePoints());
      }

      hand.purchasedCards().remove(cardToSacrifice);
      hand.decrementPrestigePoints(cardToSacrifice.prestigePoints());
    } else {
      // Check that the player owns the cards
      if (!hand.purchasedCards().contains(cardToSacrifice1)
          || !hand.purchasedCards().contains(cardToSacrifice2)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("you must own the cards you're sacrificing");
      }

      // Check that neither card is a satchel or double bonus
      if (cardToSacrifice1 instanceof DoubleBonusCard || cardToSacrifice1 instanceof SatchelCard
          || cardToSacrifice2 instanceof DoubleBonusCard
          || cardToSacrifice2 instanceof SatchelCard) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot sacrifice a double bonus card or satchel card "
                  + "if you're sacrificing two cards");
      }

      // Check that the player cannot sacrifice a satchel card instead
      if (hand.purchasedCards().stream()
          .anyMatch(e -> e instanceof SatchelCard satchel
                         && GameBoardHelper.matchesCardDiscountColor(cardToPurchase,
              satchel.cardToAttach()))) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("must sacrifice a satchel card if possible");
      }

      // Check that both cards are of the same color as the card to purchase
      if (!GameBoardHelper.matchesCardDiscountColor(cardToPurchase, cardToSacrifice1)
          || !GameBoardHelper.matchesCardDiscountColor(cardToPurchase, cardToSacrifice2)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("discount color must match the purchased card's discount color");
      }

      hand.purchasedCards().remove(cardToSacrifice1);
      hand.purchasedCards().remove(cardToSacrifice2);
      hand.decrementPrestigePoints(
          cardToSacrifice1.prestigePoints() + cardToSacrifice2.prestigePoints());
      if (!GameBoardHelper.decrementGemDiscounts(cardToSacrifice1, hand)
          || !GameBoardHelper.decrementGemDiscounts(cardToSacrifice2, hand)) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("failed to decrement prestige points");
      }
    }

    if (reserved && !(hand.reservedCards().remove(card))) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("failed to remove card from reserved cards");
    } else {
      GameBoardHelper.removeCardFromDeck(gameBoard.cards(), card);
    }

    // Increment the player's prestige points
    hand.incrementPrestigePoints(card.prestigePoints());
    // Add the card to the player's hand
    hand.purchasedCards().add(card);
    hand.gemDiscounts().merge(cardToPurchase.discountColor(),
        Bonus.SINGLE.getValue(), Integer::sum);

    return getStringResponseEntity(gameBoard, hand);
  }

  private ResponseEntity<String> handlePayment(GameBoard gameBoard, Hand hand, Card card,
                                               GemPayment payment, boolean reserved) {
    if (card instanceof SacrificeCard) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot pay for a card with gems if it is a sacrifice card");
    }

    // Check that they have the amount of gold gem cards they say they do
    final int numGoldGemCards = payment.getNumGoldGemCards();
    if (numGoldGemCards < 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("numGoldGemCards cannot be less than 0");
    } else if (numGoldGemCards
               > hand.purchasedCards().stream().filter(c -> c instanceof GoldGemCard).count()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("do not have enough gold gem cards");
    }


    final Gems gemsToPayWith = payment.getChosenGems() == null ? new Gems() :
        payment.getChosenGems();

    // Add the gold gems from the gold gem cards to the chosen gems to pay with
    final Gems gemsWithExtraGoldGems = new Gems(gemsToPayWith);
    gemsWithExtraGoldGems.compute(GemColor.GOLD,
        (k, v) -> v == null ? (numGoldGemCards == 0 ? null : numGoldGemCards * 2)
            : Integer.valueOf(v + numGoldGemCards * 2));

    final Gems substitutedGems = payment.getSubstitutedGems() == null ? new Gems() :
        payment.getSubstitutedGems();
    if (substitutedGems.containsKey(GemColor.GOLD)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot substitute gold gems for gold gems");
    }

    final int substitutedGemsCount = GameBoardHelper.countGemAmount(substitutedGems);
    if ((substitutedGemsCount != gemsWithExtraGoldGems.getOrDefault(GemColor.GOLD, 0)
         && numGoldGemCards == 0)
        || (numGoldGemCards != 0
            && substitutedGemsCount != gemsWithExtraGoldGems.get(GemColor.GOLD) - 1)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("substituting amount not equal to gold gems");
    }

    // If they wasted a gold token, remove it from gemsWithExtraGoldGems
    if (numGoldGemCards != 0
        && substitutedGemsCount == gemsWithExtraGoldGems.get(GemColor.GOLD) - 1) {
      gemsWithExtraGoldGems.merge(GemColor.GOLD, 1, (v, n) -> v - n);
    }

    final Gems ownedGems = hand.gems();
    if (!ownedGems.hasEnoughGems(gemsToPayWith)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not enough gems");
    }

    // Verify that the amount of gems is enough to buy the card
    final Gems discountedCost = GameBoardHelper.getDiscountedCost(card.cost(), hand.gemDiscounts());
    if (!discountedCost.equals(
        GameBoardHelper.getPaymentWithoutGoldGems(substitutedGems, gemsWithExtraGoldGems))) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("card cost does not match payment");
    }

    // Now we know that they can pay for this card

    if (card instanceof DoubleBonusCard c) {
      hand.gemDiscounts().merge(c.discountColor(), Bonus.DOUBLE.getValue(), Integer::sum);
    } else if (card instanceof StandardCard c) {
      hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
    } else if (card instanceof ReserveNobleCard c) {
      final ResponseEntity<String> response = GameBoardHelper.reserveNoble(gameBoard, hand, c);
      if (response != null) {
        return response;
      }
    } else if (card instanceof SatchelCard c) {
      final ResponseEntity<String> response =
          GameBoardHelper.purchaseSatchelCard(c, hand, gameBoard);
      if (response != null) {
        return response;
      }
    } else if (card instanceof WaterfallCard c) { // If card is null
      if (c.cardToTake() == null) {
        // If there are level two cards available to take
        if (gameBoard.cards().stream()
            .anyMatch(l -> !l.isEmpty() && l.get(0).level() == CardLevel.TWO)) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("card to take cannot be null");
        }
      } else if (c.cardToTake().level() != CardLevel.TWO) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("must take a level two card for free");
      } else if (!GameBoardHelper.isCardFaceUpOnBoard(gameBoard.cards(), c.cardToTake())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("card to take is not available");
      }

      if (c.cardToTake() != null) {
        final ResponseEntity<String> response =
            GameBoardHelper.purchaseFreeCard(c.cardToTake(), hand, gameBoard);

        if (response != null) {
          return response;
        }
      }
      // Add gem discounts
      hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
      c.removeCardToTake();
    }

    if (reserved && !(hand.reservedCards().remove(card))) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("failed to remove card from reserved cards");
    } else {
      GameBoardHelper.removeCardFromDeck(gameBoard.cards(), card);
    }

    // Remove the chosen gold gem cards from the player's hand
    GameBoardHelper.removeGoldGemCards(payment.getNumGoldGemCards(), hand);
    // Take gems from player
    GameBoardHelper.removeGems(ownedGems, gemsToPayWith);
    // Add gems to bank
    GameBoardHelper.addGems(gameBoard.availableGems(), gemsToPayWith);
    // Increment the player's prestige points
    hand.incrementPrestigePoints(card.prestigePoints());
    // Add the card to the player's hand
    hand.purchasedCards().add(card);

    return getStringResponseEntity(gameBoard, hand);
  }

  /**
   * Reserve a given card.
   *
   * @param gameid            The game id corresponding to the game.
   * @param accessToken       The access token belonging to the player trying to reserve the card.
   * @param reserveCardString The reserve card json string containing the form.
   * @return The response.
   */
  @PutMapping(value = "{gameid}/card/reserve", consumes = MediaType.ALL_VALUE)
  public ResponseEntity<String> reserveCard(@PathVariable String gameid,
                                            @RequestParam("access_token") String accessToken,
                                            @RequestBody String reserveCardString) {
    final ReserveCardForm reserveCardForm =
        gsonInstance.gson.fromJson(reserveCardString, ReserveCardForm.class);

    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(gameid);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    } else if (gameBoard.isGameOver()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("game is over");
    }

    final Hand hand = GameBoardHelper.getHand(gameBoard.players(), username);
    if (hand == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("player is not part of this game");
    }

    if (hand.reservedCards().size() >= 3) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cannot reserve more than 3 cards");
    }

    Card card = reserveCardForm.card();
    if (card == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("card cannot be null");
    }
    final Set<List<Card>> decks = gameBoard.cards();
    if (!reserveCardForm.isTakingFaceDown() && !GameBoardHelper.isCardFaceUpOnBoard(decks, card)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("card chosen for reservation is not valid");
    }

    final Gems availableGems = gameBoard.availableGems();
    final int gemAmount = GameBoardHelper.countGemAmount(hand.gems());
    final GemColor gemColor = reserveCardForm.gemColor();

    if (gemAmount < 10 && gemColor != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot get rid of a gem if you have less than 10 gems");
    } else if (gemAmount == 10 && gemColor != null && !hand.gems().containsKey(gemColor)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot get rid of a gem that you do not have");
    } else if (!availableGems.containsKey(GemColor.GOLD) && gemColor != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot get rid of a gem if there are no gold gems in the bank");
    }

    // Remove the card from the game board and add it to the player's reserved stack
    if (reserveCardForm.isTakingFaceDown()) {
      card = GameBoardHelper.getFaceDownCard(decks, card);
      if (card == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("not enough cards in the deck to reserve a face down card");
      }
    } else if (!GameBoardHelper.removeCardFromDeck(decks, card)) {
      // This should never happen
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    hand.reservedCards().add(card);

    if (availableGems.containsKey(GemColor.GOLD)
        && ((gemAmount == 10 && gemColor != null) || gemAmount < 10)) {
      hand.gems().merge(GemColor.GOLD, 1, Integer::sum);
      // Remove the GOLD mapping if it reaches 0
      availableGems.computeIfPresent(GemColor.GOLD, (k, v) -> v == 1 ? null : v - 1);

      if (gemColor != null) {
        hand.gems().computeIfPresent(gemColor, (k, v) -> v == 1 ? null : v - 1);
        availableGems.merge(gemColor, 1, Integer::sum);
      }
    }

    return getStringResponseEntity(gameBoard, hand);
  }

  private ResponseEntity<String> getStringResponseEntity(GameBoard gameBoard, Hand hand) {
    final Set<Noble> nobles = gameBoard.computeClaimableNobles(hand);
    gameSpecificBroadcastManagers.get(gameBoard.gameid()).touch();
    if (!nobles.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(gsonInstance.gson.toJson(nobles));
    } else {
      return getStringResponseEntity(gameBoard);
    }
  }

  private static ResponseEntity<String> getStringResponseEntity(GameBoard gameBoard) {
    final boolean isLastRound = gameBoard.nextTurn();

    if (gameBoard.isGameOver()) {
      return ResponseEntity.status(HttpStatus.OK).body("game is over");
    } else if (isLastRound) {
      return ResponseEntity.status(HttpStatus.OK).body("last round");
    }
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  /**
   * Take gems.
   *
   * @param gameid             The game id corresponding to the game.
   * @param accessToken        The access token belonging to the player trying to take gems.
   * @param takeGemsFormString The take gems form.
   * @return The response.
   */
  @PutMapping(value = "{gameid}/gems", consumes = MediaType.ALL_VALUE)
  public ResponseEntity<String> takeGems(@PathVariable String gameid,
                                         @RequestParam("access_token") String accessToken,
                                         @RequestBody String takeGemsFormString) {
    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(gameid);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    } else if (gameBoard.isGameOver()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("game is over");
    }

    final Hand hand = GameBoardHelper.getHand(gameBoard.players(), username);
    if (hand == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("player is not part of this game");
    }

    final TakeGemsForm takeGemsForm =
        gsonInstance.gson.fromJson(takeGemsFormString, TakeGemsForm.class);
    final Gems gemsToTake = takeGemsForm.gemsToTake();
    if (gemsToTake == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("gems to take cannot be null");
    } else if (gemsToTake.get(GemColor.GOLD) != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cannot take gold gems");
    }

    final Gems gemsToRemove = takeGemsForm.gemsToRemove();
    final int amountOfGemsToTake = GameBoardHelper.countGemAmount(gemsToTake);
    final int amountOfGemsInHand = GameBoardHelper.countGemAmount(hand.gems());
    if (amountOfGemsToTake > 3) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cannot take more than 3 gems");
    } else if (!gameBoard.availableGems().hasEnoughGems(gemsToTake)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not enough gems in the bank");
    } else {
      final Gems gemsAfterTaking = new Gems(hand.gems());
      GameBoardHelper.addGems(gemsAfterTaking, gemsToTake);
      if (gemsToRemove != null && !gemsAfterTaking.hasEnoughGems(gemsToRemove)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot remove gems that you do not own");
      }
      final int total = amountOfGemsToTake + amountOfGemsInHand;
      if (total > 10 && (gemsToRemove == null
                         || (total - GameBoardHelper.countGemAmount(gemsToRemove) > 10))) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("can only have a maximum of 10 gems");
      } else if (total <= 10 && gemsToRemove != null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot remove gems if not necessary");
      } else if (total > 10 && total - GameBoardHelper.countGemAmount(gemsToRemove) < 10) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot remove gems and be left with less than 10");
      }
    }

    final long sizeOfBankWithoutGoldGems =
        new Gems(gameBoard.availableGems()).keySet().stream().filter(e -> e != GemColor.GOLD)
            .count();
    if (gemsToTake.size() == 1) {
      final GemColor gemColorToTake = gemsToTake.entrySet().iterator().next().getKey();
      final int amountOfGemsAvailable = gameBoard.availableGems().get(gemColorToTake);
      if (amountOfGemsToTake > 2) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot take 3 gems of one color");
      } else if (amountOfGemsAvailable < 4 && amountOfGemsToTake == 2) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            "cannot take 2 same color gems, there are less than 4 gems of that color in the bank");
      } else if (amountOfGemsToTake == 1
                 && (sizeOfBankWithoutGoldGems > 1 || amountOfGemsAvailable >= 4)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot take 1 gem if it is possible to take more");
      }
      // Only possible scenarios now
      // amountOfGemsAvailable >= 4 && amountOfGemsToTake == 2
      // amountOfGemsAvailable < 4 && amountOfGemsToTake == 1 && only 1 gem color available
    } else if (gemsToTake.values().stream().anyMatch(v -> v > 1)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot take more than one gem of each gem");
    } else if (gemsToTake.size() == 2 && sizeOfBankWithoutGoldGems != 2) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "cannot take 1 gem of each for 2 gem colors if it is possible to take from 3 colors");
    }

    // Take gems from the bank and add them to the player
    GameBoardHelper.removeGems(gameBoard.availableGems(), gemsToTake);
    GameBoardHelper.addGems(hand.gems(), gemsToTake);

    // If necessary, take excess gems from the player and add them to the bank
    if (gemsToRemove != null) {
      GameBoardHelper.removeGems(hand.gems(), gemsToRemove);
      GameBoardHelper.addGems(gameBoard.availableGems(), gemsToRemove);
    }

    return getStringResponseEntity(gameBoard, hand);
  }

  /**
   * Claim a noble.
   *
   * @param gameid               The game id corresponding to the game.
   * @param accessToken          The access token belonging to the player reserving the card.
   * @param claimNobleFormString The claim noble form.
   * @return The response.
   */
  @PutMapping(value = "{gameid}/noble", consumes = MediaType.ALL_VALUE)
  public ResponseEntity<String> claimNoble(@PathVariable String gameid,
                                           @RequestParam("access_token") String accessToken,
                                           @RequestBody String claimNobleFormString) {
    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(gameid);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    } else if (gameBoard.isGameOver()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("game is over");
    }

    final Hand hand = GameBoardHelper.getHand(gameBoard.players(), username);
    if (hand == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("player is not part of this game");
    }

    final ClaimNobleForm claimNobleForm =
        gsonInstance.gson.fromJson(claimNobleFormString, ClaimNobleForm.class);
    final Noble nobleToClaim = claimNobleForm.nobleToClaim();
    if (nobleToClaim == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("noble to claim cannot be null");
    }

    if (!hand.gemDiscounts().hasEnoughGems(nobleToClaim.cost())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("do not have enough gem discounts to claim noble");
    }

    if (gameBoard.availableNobles().contains(nobleToClaim)) {
      gameBoard.availableNobles().remove(nobleToClaim);
    } else if (hand.reservedNobles().contains(nobleToClaim)) {
      hand.reservedNobles().remove(nobleToClaim);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("noble to claim is not available");
    }
    hand.visitedNobles().add(nobleToClaim);
    hand.incrementPrestigePoints(nobleToClaim.prestigePoints());

    return getStringResponseEntity(gameBoard);
  }
}