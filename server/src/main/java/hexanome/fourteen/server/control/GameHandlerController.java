package hexanome.fourteen.server.control;

import hexanome.fourteen.server.Mapper;
import hexanome.fourteen.server.control.form.ClaimNobleForm;
import hexanome.fourteen.server.control.form.LaunchGameForm;
import hexanome.fourteen.server.control.form.PurchaseCardForm;
import hexanome.fourteen.server.control.form.ReserveCardForm;
import hexanome.fourteen.server.control.form.SaveGameForm;
import hexanome.fourteen.server.control.form.TakeGemsForm;
import hexanome.fourteen.server.model.User;
import hexanome.fourteen.server.model.board.GameBoard;
import hexanome.fourteen.server.model.board.Hand;
import hexanome.fourteen.server.model.board.Noble;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import hexanome.fourteen.server.model.sent.SentGameBoard;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
    Set<Noble> nobles = null; // TODO

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
   * @param gameid           The game id corresponding to the game.
   * @param accessToken      The access token belonging to the player trying to purchase the card.
   * @param purchaseCardForm The purchase card form.
   * @return The response.
   */
  @PutMapping(value = "{gameid}/card/purchase", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> purchaseCard(@PathVariable String gameid,
                                             @RequestParam("access_token") String accessToken,
                                             @RequestBody PurchaseCardForm purchaseCardForm) {
    // TODO check what type of card it is and perform the relevant action
    //  check if the card is reserved, must specify it in the form in case
    //  the cards are duplicated on the board and reserved
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
    }
    final Set<List<Card>> decks = gameBoard.cards();
    if (!cardIsFaceUpOnBoard(decks, card)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("card chosen for purchase is not valid");
    }

    final Gems substitutedGems = purchaseCardForm.substitutedGems() == null ? new Gems() :
        purchaseCardForm.substitutedGems();
    if (substitutedGems.containsKey(GemColor.GOLD)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot substitute gold gems for gold gems");
    }

    final Gems gemsToPayWith =
        purchaseCardForm.gemsToPayWith() == null ? new Gems() : purchaseCardForm.gemsToPayWith();
    if (countGemAmount(substitutedGems) != gemsToPayWith.getOrDefault(GemColor.GOLD, 0)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("substituting amount not equal to gold gems");
    }

    final Gems ownedGems = hand.gems();
    if (!hasEnoughGems(ownedGems, gemsToPayWith)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not enough gems");
    }

    // verify that the amount of gems is enough to buy the card
    final Gems discountedCost = getDiscountedCost(card.cost(), hand.gemDiscounts());
    if (!discountedCost.equals(getPaymentWithoutGoldGems(substitutedGems, gemsToPayWith))) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("card cost does not match payment");
    }

    // Remove the card from the game board
    if (!removeCardFromDeck(decks, card)) {
      // This should never happen
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    // Increment the player's prestige points
    hand.incrementPrestigePoints(card.prestigePoints());
    // Add the card to the player's hand
    hand.purchasedCards().add(card);
    // Take gems from player
    removeGems(ownedGems, gemsToPayWith);
    // Add gems to bank
    addGems(gameBoard.availableGems(), gemsToPayWith);
    // Compute the leading player
    gameBoard.computeLeadingPlayer();

    gameBoard.nextTurn();
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  /**
   * Reserve a given card.
   *
   * @param gameid          The game id corresponding to the game.
   * @param accessToken     The access token belonging to the player trying to reserve the card.
   * @param reserveCardForm The reserve card form.
   * @return The response.
   */
  @PutMapping(value = "{gameid}/card/reserve", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> reserveCard(@PathVariable String gameid,
                                            @RequestParam("access_token") String accessToken,
                                            @RequestBody ReserveCardForm reserveCardForm) {
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
    if (!reserveCardForm.isTakingFaceDown() && !cardIsFaceUpOnBoard(decks, card)) {
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
      hand.gems().compute(GemColor.GOLD, (k, v) -> v == null ? 1 : v + 1);
      // Remove the GOLD mapping if it reaches 0
      availableGems.computeIfPresent(GemColor.GOLD, (k, v) -> v == 1 ? null : v - 1);

      if (gemColor != null) {
        hand.gems().computeIfPresent(gemColor, (k, v) -> v == 1 ? null : v - 1);
        availableGems.compute(gemColor, (k, v) -> v == null ? 1 : v + 1);
      }
    }

    gameBoard.nextTurn();
    return ResponseEntity.status(HttpStatus.OK).body(null);
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
    } else if (!hasEnoughGems(gameBoard.availableGems(), gemsToTake)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not enough gems in the bank");
    } else {
      final Gems gemsAfterTaking = new Gems(hand.gems());
      addGems(gemsAfterTaking, gemsToTake);
      if (gemsToRemove != null && !hasEnoughGems(gemsAfterTaking, gemsToRemove)) {
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

    gameBoard.nextTurn();
    return ResponseEntity.status(HttpStatus.OK).body(null);
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

    // TODO check if they can actually claim said noble, assume the frontend is doing that for now

    if (gameBoard.availableNobles().contains(nobleToClaim)) {
      gameBoard.availableNobles().remove(nobleToClaim);
    } else if (nobleToClaim.equals(hand.reservedNoble())) {
      hand.setReservedNoble(null);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("noble to claim is not available");
    }
    hand.visitedNobles().add(nobleToClaim);
    hand.incrementPrestigePoints(nobleToClaim.prestigePoints());

    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  /**
   * Has a side effect of removing the face down card from the deck if successful.
   */
  private Card getFaceDownCard(Set<List<Card>> decks, Card card) {
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

  private Gems getDiscountedCost(Gems originalCost, Gems gemDiscounts) {
    final Gems result = new Gems(originalCost);

    // Remove the gem discounts from the cost of the card to get the cost for this specific player
    removeGems(result, gemDiscounts);

    return result;
  }

  private void addGems(Gems gemsToAddTo, Gems gemsToAdd) {
    gemsToAdd.forEach(
        (key, value) -> gemsToAddTo.compute(key, (k, v) -> v == null ? value : v + value));
  }

  private void removeGems(Gems gemsToRemoveFrom, Gems gemsToRemove) {
    gemsToRemove.forEach((key, amountToRemove) -> gemsToRemoveFrom.computeIfPresent(key,
        (k, v) -> v.equals(amountToRemove) ? null : v - amountToRemove));
  }

  private boolean removeCardFromDeck(Set<List<Card>> decks, Card card) {
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

  private Gems getPaymentWithoutGoldGems(Gems substitutedGems, Gems gemsToPayWith) {
    final Gems result = new Gems(gemsToPayWith);
    result.remove(GemColor.GOLD);

    substitutedGems.forEach(
        (key, value) -> result.compute(key, (k, v) -> v == null ? value : v + value));

    return result;
  }

  private boolean cardIsFaceUpOnBoard(Set<List<Card>> decks, Card card) {
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

  private int countGemAmount(Gems gems) {
    return gems.values().stream().mapToInt(value -> value).sum();
  }

  private boolean hasEnoughGems(Gems ownedGems, Gems gemsToPayWith) {
    return gemsToPayWith.entrySet().stream()
        .noneMatch(entry -> ownedGems.getOrDefault(entry.getKey(), 0) < entry.getValue());
  }

  private Hand getHand(Collection<Player> players, String username) {
    return players.stream().filter(p -> p.uid().equals(username)).findFirst().map(Player::hand)
        .orElse(null);
  }
}