package hexanome.fourteen.server.control;

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
import hexanome.fourteen.server.model.board.Hand;
import hexanome.fourteen.server.model.board.Noble;
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
import hexanome.fourteen.server.model.sent.SentGameBoard;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

/**
 * Controller to handle lobby service callbacks.
 */
@RestController
@RequestMapping("/api/games/")
public class GameHandlerController {

  private final LobbyServiceCaller lobbyService;
  private final Map<String, GameBoard> gameManager;
  private final Mapper<User, Player> userPlayerMapper;
  private final Mapper<GameBoard, SentGameBoard> gameBoardMapper;
  private final GsonInstance gsonInstance;
  private final SavedGamesService saveGameManager;
  private final ServerService serverService;

  /**
   * Constructor.
   *
   * @param lobbyService     Lobby service
   * @param userPlayerMapper The User to Player Mapper
   * @param gameBoardMapper  The GameBard Mapper
   * @param gsonInstance     The GSON we will be using
   */
  public GameHandlerController(@Autowired LobbyServiceCaller lobbyService,
                               @Autowired Mapper<User, Player> userPlayerMapper,
                               @Autowired Mapper<GameBoard, SentGameBoard> gameBoardMapper,
                               @Autowired GsonInstance gsonInstance,
                               @Autowired SavedGamesService saveGameManager,
                               @Autowired ServerService serverService) {
    this.lobbyService = lobbyService;
    this.userPlayerMapper = userPlayerMapper;
    this.gameBoardMapper = gameBoardMapper;
    this.gsonInstance = gsonInstance;
    this.saveGameManager = saveGameManager;
    this.serverService = serverService;
    gameManager = new HashMap<>();
  }

  /**
   * Create a game given the specified parameters.
   *
   * @param gameid         The game id for the created game
   * @param launchGameForm The information needed to create the game
   * @return The full response
   */
  @PutMapping(value = "{gameid}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchGame(@PathVariable String gameid,
                                           @RequestBody LaunchGameForm launchGameForm) {
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
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  private GameBoard createGame(String gameid, LaunchGameForm launchGameForm) {
    Set<Noble> nobles = new HashSet<>(); // TODO

    Set<Expansion> expansions =
        GameServiceName.getExpansions(GameServiceName.valueOf(launchGameForm.gameType()));
    Set<Player> players = Arrays.stream(launchGameForm.players()).map(userPlayerMapper::map)
        .collect(Collectors.toSet());
    String creator = launchGameForm.creator();

    return new GameBoard(nobles, expansions, players, gameid, creator);
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
  public ResponseEntity<String> retrieveGame(@PathVariable String gameid,
                                             @RequestParam("access_token") String accessToken) {
    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(gameid);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    } else if (getHand(gameBoard.players(), username) == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("player is not part of this game");
    } else {
      final SentGameBoard sentGameBoard = gameBoardMapper.map(gameBoard);
      return ResponseEntity.status(HttpStatus.OK)
          .body(gsonInstance.gson.toJson(sentGameBoard, SentGameBoard.class));
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
    } else if (getHand(gameBoard.players(), username) == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("player is not part of this game");
    } else {
      if (!serverService.refreshToken()) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("token could not be refreshed");
      }
      String saveGameid = saveGameManager.putGame(gameBoard);
      lobbyService.saveGame(serverService.accessToken,
          new SaveGameForm(GameServiceName.getGameServiceName(gameBoard.expansions()).name(),
              gameBoard.players().stream().map(Player::uid).toList(), saveGameid));
      return ResponseEntity.status(HttpStatus.OK).body(null);
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
  @PutMapping(value = "{gameid}/card/purchase", consumes = "text/plain; charset=utf-8")
  public ResponseEntity<String> purchaseCard(@PathVariable String gameid,
                                             @RequestParam("access_token") String accessToken,
                                             @RequestBody String purchaseCardString) {
    // TODO check what type of card it is and perform the relevant action
    //  check if the card is reserved, must specify it in the form in case
    //  the cards are duplicated on the board and reserved
    final PurchaseCardForm purchaseCardForm =
        gsonInstance.gson.fromJson(purchaseCardString, PurchaseCardForm.class);

    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(gameid);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    }

    final Hand hand = getHand(gameBoard.players(), username);
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
    if (!isCardFaceUpOnBoard(decks, card) && !purchaseCardForm.isReserved()) {
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
                           && matchesCardDiscountColor(cardToPurchase, satchel.cardToAttach()))) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("must sacrifice a satchel card if possible");
        }

        if (doubleBonusCard.discountColor() != cardToPurchase.discountColor()) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("sacrifice card discount color must equal purchased card discount color");
        }

        hand.gemDiscounts().computeIfPresent(doubleBonusCard.discountColor(),
            (k, v) -> v == doubleBonusCard.gemDiscount() ? null :
                v - doubleBonusCard.gemDiscount());
      } else {
        final SatchelCard satchelCard = (SatchelCard) cardToSacrifice;
        if (!matchesCardDiscountColor(cardToPurchase, satchelCard.cardToAttach())) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("discount color must match the purchased card's discount color");
        }

        // Decrement gem discounts
        switch (satchelCard.cardToAttach()) {
          case ReserveNobleCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
              (k, v) -> v == c.gemDiscount() + 1 ? null : v - c.gemDiscount() - 1);
          case DoubleBonusCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
              (k, v) -> v == c.gemDiscount() + 1 ? null : v - c.gemDiscount() - 1);
          case SacrificeCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
              (k, v) -> v == c.gemDiscount() + 1 ? null : v - c.gemDiscount() - 1);
          case StandardCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
              (k, v) -> v == c.gemDiscount() + 1 ? null : v - c.gemDiscount() - 1);
          case WaterfallCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
              (k, v) -> v == c.gemDiscount() + 1 ? null : v - c.gemDiscount() - 1);
          case default -> {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("failed to decrement prestige points");
          }
        }
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

      // Check that the player cannot sacrifice a satchel card instead
      if (hand.purchasedCards().stream()
          .anyMatch(e -> e instanceof SatchelCard satchel
                         && matchesCardDiscountColor(cardToPurchase, satchel.cardToAttach()))) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("must sacrifice a satchel card if possible");
      }

      // Check that neither card is a satchel or double bonus
      if (cardToSacrifice1 instanceof DoubleBonusCard || cardToSacrifice1 instanceof SatchelCard
          || cardToSacrifice2 instanceof DoubleBonusCard
          || cardToSacrifice2 instanceof SatchelCard) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot sacrifice a double bonus card or satchel card "
                  + "if you're sacrificing two cards");
      }

      // Check that both cards are of the same color as the card to purchase
      if (!matchesCardDiscountColor(cardToPurchase, cardToSacrifice1)
          || !matchesCardDiscountColor(cardToPurchase, cardToSacrifice2)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("discount color must match the purchased card's discount color");
      }

      hand.purchasedCards().remove(cardToSacrifice1);
      hand.purchasedCards().remove(cardToSacrifice2);
      hand.decrementPrestigePoints(
          cardToSacrifice1.prestigePoints() + cardToSacrifice2.prestigePoints());
      if (!decrementGemDiscounts(cardToSacrifice1, hand)
          || !decrementGemDiscounts(cardToSacrifice2, hand)) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("failed to decrement prestige points");
      }
    }

    if (reserved && !(hand.reservedCards().remove(card))) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("failed to remove card from reserved cards");
    } else {
      removeCardFromDeck(gameBoard.cards(), card);
    }

    // Increment the player's prestige points
    hand.incrementPrestigePoints(card.prestigePoints());
    // Add the card to the player's hand
    hand.purchasedCards().add(card);
    hand.gemDiscounts().merge(cardToPurchase.discountColor(),
        cardToPurchase.gemDiscount(), Integer::sum);

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

    final int substitutedGemsCount = countGemAmount(substitutedGems);
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
    final Gems discountedCost = getDiscountedCost(card.cost(), hand.gemDiscounts());
    if (!discountedCost.equals(getPaymentWithoutGoldGems(substitutedGems, gemsWithExtraGoldGems))) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("card cost does not match payment");
    }

    // Now we know that they can pay for this card

    switch (card) {
      case DoubleBonusCard c ->
          hand.gemDiscounts().merge(c.discountColor(), c.gemDiscount(), Integer::sum);
      case StandardCard c ->
          hand.gemDiscounts().merge(c.discountColor(), c.gemDiscount(), Integer::sum);
      case ReserveNobleCard c -> {
        final ResponseEntity<String> response = reserveNoble(gameBoard, hand, c);
        if (response != null) {
          return response;
        }
      }
      case SatchelCard c -> {
        final ResponseEntity<String> response = purchaseSatchelCard(c, hand, gameBoard);
        if (response != null) {
          return response;
        }
      }
      case WaterfallCard c -> {
        // If card is null
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
        } else if (!isCardFaceUpOnBoard(gameBoard.cards(), c.cardToTake())) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("card to take is not available");
        }

        if (c.cardToTake() != null) {
          final ResponseEntity<String> response =
              purchaseFreeCard(c.cardToTake(), hand, gameBoard);

          if (response != null) {
            return response;
          }
        }
        // Add gem discounts
        hand.gemDiscounts().merge(c.discountColor(), c.gemDiscount(), Integer::sum);
        c.removeCardToTake();
      }
      case default -> {
      }
    }

    if (reserved && !(hand.reservedCards().remove(card))) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("failed to remove card from reserved cards");
    } else {
      removeCardFromDeck(gameBoard.cards(), card);
    }

    // Remove the chosen gold gem cards from the player's hand
    removeGoldGemCards(payment.getNumGoldGemCards(), hand);
    // Take gems from player
    removeGems(ownedGems, gemsToPayWith);
    // Add gems to bank
    addGems(gameBoard.availableGems(), gemsToPayWith);
    // Increment the player's prestige points
    hand.incrementPrestigePoints(card.prestigePoints());
    // Add the card to the player's hand
    hand.purchasedCards().add(card);

    return getStringResponseEntity(gameBoard, hand);
  }

  private static ResponseEntity<String> purchaseSatchelCard(SatchelCard card, Hand hand,
                                                            GameBoard gameBoard) {
    if (card.cardToAttach() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("card to attach cannot be null");
    }

    switch (card.level()) {
      case ONE -> {
        if (card.freeCardToTake() != null) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("level one satchel card does not allow you to take a free card");
        }
        // Now must add extra gem discount
        final ResponseEntity<String> response = addAttachedCardGemDiscounts(card, hand);
        if (response != null) {
          return response;
        }
      }
      case TWO -> {
        if (card.freeCardToTake() == null) {
          if (gameBoard.cards().stream()
              .anyMatch(l -> !l.isEmpty() && l.get(0).level() == CardLevel.ONE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("card to take cannot be null");
          }
          // Cannot take a free card because there are none to take
        } else {
          if (card.freeCardToTake().level() != CardLevel.ONE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("can only take a free level one card from a level two satchel card");
          }
          final ResponseEntity<String> response =
              purchaseFreeCard(card.freeCardToTake(), hand, gameBoard);
          if (response != null) {
            return response;
          }
        }
        // Now must add extra gem discount
        final ResponseEntity<String> response = addAttachedCardGemDiscounts(card, hand);
        if (response != null) {
          return response;
        }
      }
      case default -> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("satchel card configured to the wrong level");
      }
    }
    card.removeFreeCardToTake();
    return null;
  }

  private static ResponseEntity<String> addAttachedCardGemDiscounts(SatchelCard card, Hand hand) {
    if (!hand.purchasedCards().contains(card.cardToAttach())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot attach a satchel card to a card that you don't own");
    }

    switch (card.cardToAttach()) {
      case StandardCard c -> hand.gemDiscounts().merge(c.discountColor(), 1, Integer::sum);
      case DoubleBonusCard c -> hand.gemDiscounts().merge(c.discountColor(), 1, Integer::sum);
      case ReserveNobleCard c -> hand.gemDiscounts().merge(c.discountColor(), 1, Integer::sum);
      case default -> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot attach a card that has no gem discount to a satchel card");
      }
    }
    return null;
  }

  private static ResponseEntity<String> reserveNoble(GameBoard gameBoard, Hand hand,
                                                     ReserveNobleCard card) {
    if (card.nobleToReserve() == null) {
      if (!gameBoard.availableNobles().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("noble to reserve cannot be null");
      }
    } else if (!gameBoard.availableNobles().contains(card.nobleToReserve())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("noble to reserve is not available");
    }

    // Add gem discounts
    hand.gemDiscounts().merge(card.discountColor(), card.gemDiscount(), Integer::sum);
    // Add reserved noble to hand
    if (card.nobleToReserve() != null) {
      hand.reservedNobles().add(card.nobleToReserve());
    }
    card.removeNobleToReserve();
    return null;
  }

  private static ResponseEntity<String> purchaseFreeCard(Card card, Hand hand,
                                                         GameBoard gameBoard) {
    if (!isCardFaceUpOnBoard(gameBoard.cards(), card)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("free card to take is not available");
    }

    switch (card) {
      case DoubleBonusCard c ->
          hand.gemDiscounts().merge(c.discountColor(), c.gemDiscount(), Integer::sum);
      case StandardCard c ->
          hand.gemDiscounts().merge(c.discountColor(), c.gemDiscount(), Integer::sum);
      case ReserveNobleCard c -> {
        final ResponseEntity<String> response = reserveNoble(gameBoard, hand, c);
        if (response != null) {
          return response;
        }
      }
      case SatchelCard c -> {
        final ResponseEntity<String> response = purchaseSatchelCard(c, hand, gameBoard);
        if (response != null) {
          return response;
        }
      }
      case GoldGemCard ignored -> {
      }
      case default -> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("waterfall card or sacrifice card configured to the wrong level");
      }
    }
    // Increment the player's prestige points
    hand.incrementPrestigePoints(card.prestigePoints());
    // Add the card to the player's hand
    hand.purchasedCards().add(card);
    // Remove the card from the deck
    removeCardFromDeck(gameBoard.cards(), card);
    return null;
  }

  private static void removeGoldGemCards(int numGoldGemCards, Hand hand) {
    for (int i = 0; numGoldGemCards > 0; i++) {
      if (hand.purchasedCards().get(i) instanceof GoldGemCard) {
        hand.purchasedCards().remove(i);
        numGoldGemCards--;
      }
    }
  }

  private static boolean decrementGemDiscounts(Card card, Hand hand) {
    switch (card) {
      case ReserveNobleCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == c.gemDiscount() ? null : v - c.gemDiscount());
      case DoubleBonusCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == c.gemDiscount() ? null : v - c.gemDiscount());
      case SacrificeCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == c.gemDiscount() ? null : v - c.gemDiscount());
      case StandardCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == c.gemDiscount() ? null : v - c.gemDiscount());
      case WaterfallCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == c.gemDiscount() ? null : v - c.gemDiscount());
      case default -> {
        return false;
      }
    }
    return true;
  }

  private static boolean matchesCardDiscountColor(SacrificeCard sacrificeCard,
                                                  Card card) {
    return switch (card) {
      case ReserveNobleCard c -> c.discountColor() == sacrificeCard.discountColor();
      case DoubleBonusCard c -> c.discountColor() == sacrificeCard.discountColor();
      case SacrificeCard c -> c.discountColor() == sacrificeCard.discountColor();
      case StandardCard c -> c.discountColor() == sacrificeCard.discountColor();
      case WaterfallCard c -> c.discountColor() == sacrificeCard.discountColor();
      case default -> false;
    };
  }

  /**
   * Reserve a given card.
   *
   * @param gameid            The game id corresponding to the game.
   * @param accessToken       The access token belonging to the player trying to reserve the card.
   * @param reserveCardString The reserve card json string containing the form.
   * @return The response.
   */
  @PutMapping(value = "{gameid}/card/reserve", consumes = "text/plain; charset=utf-8")
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
    }

    final Hand hand = getHand(gameBoard.players(), username);
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
    if (!reserveCardForm.isTakingFaceDown() && !isCardFaceUpOnBoard(decks, card)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("card chosen for reservation is not valid");
    }

    final Gems availableGems = gameBoard.availableGems();
    final int gemAmount = countGemAmount(hand.gems());
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
      card = getFaceDownCard(decks, card);
      if (card == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("not enough cards in the deck to reserve a face down card");
      }
    } else if (!removeCardFromDeck(decks, card)) {
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
    if (!nobles.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(gsonInstance.gson.toJson(nobles));
    } else {
      gameBoard.nextTurn();
      return ResponseEntity.status(HttpStatus.OK).body(null);
    }
  }

  /**
   * Take gems.
   *
   * @param gameid       The game id corresponding to the game.
   * @param accessToken  The access token belonging to the player trying to take gems.
   * @param takeGemsForm The take gems form.
   * @return The response.
   */
  @PutMapping(value = "{gameid}/gems", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> takeGems(@PathVariable String gameid,
                                         @RequestParam("access_token") String accessToken,
                                         @RequestBody TakeGemsForm takeGemsForm) {
    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(gameid);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    }

    final Hand hand = getHand(gameBoard.players(), username);
    if (hand == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("player is not part of this game");
    }

    final Gems gemsToTake = takeGemsForm.gemsToTake();
    if (gemsToTake == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("gems to take cannot be null");
    } else if (gemsToTake.get(GemColor.GOLD) != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cannot take gold gems");
    }

    final Gems gemsToRemove = takeGemsForm.gemsToRemove();
    final int amountOfGemsToTake = countGemAmount(gemsToTake);
    final int amountOfGemsInHand = countGemAmount(hand.gems());
    if (amountOfGemsToTake > 3) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cannot take more than 3 gems");
    } else if (!gameBoard.availableGems().hasEnoughGems(gemsToTake)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not enough gems in the bank");
    } else {
      final Gems gemsAfterTaking = new Gems(hand.gems());
      addGems(gemsAfterTaking, gemsToTake);
      if (gemsToRemove != null && !gemsAfterTaking.hasEnoughGems(gemsToRemove)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot remove gems that you do not own");
      }
      final int total = amountOfGemsToTake + amountOfGemsInHand;
      if (total > 10 && (gemsToRemove == null || (total - countGemAmount(gemsToRemove) > 10))) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("can only have a maximum of 10 gems");
      } else if (total <= 10 && gemsToRemove != null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot remove gems if not necessary");
      } else if (total > 10 && total - countGemAmount(gemsToRemove) < 10) {
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
    removeGems(gameBoard.availableGems(), gemsToTake);
    addGems(hand.gems(), gemsToTake);

    // If necessary, take excess gems from the player and add them to the bank
    if (gemsToRemove != null) {
      removeGems(hand.gems(), gemsToRemove);
      addGems(gameBoard.availableGems(), gemsToRemove);
    }

    return getStringResponseEntity(gameBoard, hand);
  }

  /**
   * Claim a noble.
   *
   * @param gameid         The game id corresponding to the game.
   * @param accessToken    The access token belonging to the player trying to reserve the card.
   * @param claimNobleForm The claim noble form.
   * @return The response.
   */
  @PutMapping(value = "{gameid}/noble", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> claimNoble(@PathVariable String gameid,
                                           @RequestParam("access_token") String accessToken,
                                           @RequestBody ClaimNobleForm claimNobleForm) {
    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(gameid);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    }

    final Hand hand = getHand(gameBoard.players(), username);
    if (hand == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("player is not part of this game");
    }

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

    gameBoard.nextTurn();

    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  /**
   * Has a side effect of removing the face down card from the deck if successful.
   */
  private static Card getFaceDownCard(Set<List<Card>> decks, Card card) {
    for (List<Card> deck : decks) {
      if (!deck.isEmpty()) {
        final Card firstCard = deck.get(0);
        if (firstCard.level() == card.level() && firstCard.expansion() == firstCard.expansion()) {
          final int cardIndex = firstCard.expansion() == Expansion.STANDARD ? 4 : 2;
          return deck.size() <= cardIndex ? null : deck.remove(cardIndex);

        }
      }
    }
    return null;
  }

  private static Gems getDiscountedCost(Gems originalCost, Gems gemDiscounts) {
    final Gems result = new Gems(originalCost);

    // Remove the gem discounts from the cost of the card to get the cost for this specific player
    removeGems(result, gemDiscounts);

    return result;
  }

  private static void addGems(Gems gemsToAddTo, Gems gemsToAdd) {
    gemsToAdd.forEach((key, value) -> gemsToAddTo.merge(key, value, Integer::sum));
  }

  private static void removeGems(Gems gemsToRemoveFrom, Gems gemsToRemove) {
    gemsToRemove.forEach((key, amountToRemove) -> gemsToRemoveFrom.computeIfPresent(key,
        (k, v) -> v.equals(amountToRemove) ? null : v - amountToRemove));
  }

  private static boolean removeCardFromDeck(Set<List<Card>> decks, Card card) {
    for (List<Card> cards : decks) {
      if (!cards.isEmpty()) {
        final Card firstCard = cards.get(0);
        if (firstCard.level() == card.level() && firstCard.expansion() == card.expansion()) {
          return cards.remove(card);
        }
      }
    }
    return false;
  }

  private static Gems getPaymentWithoutGoldGems(Gems substitutedGems, Gems gemsToPayWith) {
    final Gems result = new Gems(gemsToPayWith);
    result.remove(GemColor.GOLD);

    substitutedGems.forEach((key, value) -> result.merge(key, value, Integer::sum));

    return result;
  }

  private static boolean isCardFaceUpOnBoard(Set<List<Card>> decks, Card card) {
    for (List<Card> cards : decks) {
      if (!cards.isEmpty()) {
        final Card firstCard = cards.get(0);
        if (firstCard.level() == card.level() && firstCard.expansion() == card.expansion()) {
          final int limit =
              Integer.min(cards.size(), firstCard.expansion() == Expansion.STANDARD ? 4 : 2);
          return cards.subList(0, limit).stream().anyMatch(cardToCheck -> cardToCheck.equals(card));
        }
      }
    }
    return false;
  }

  private static int countGemAmount(Gems gems) {
    return gems.values().stream().mapToInt(value -> value).sum();
  }

  private static Hand getHand(Collection<Player> players, String username) {
    return players.stream().filter(p -> p.uid().equals(username)).findFirst().map(Player::hand)
        .orElse(null);
  }
}