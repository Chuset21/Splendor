package hexanome.fourteen.server.control;

import hexanome.fourteen.server.Mapper;
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
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  private final String lsLocation;
  private final Map<Set<String>, GameBoard> gameManager;
  private final Mapper<User, Player> userPlayerMapper;
  private final Mapper<String, Expansion> stringExpansionMapper;
  private final Mapper<GameBoard, SentGameBoard> gameBoardMapper;
  private final GsonInstance gsonInstance;

  /**
   * Constructor.
   *
   * @param userPlayerMapper      The User to Player Mapper
   * @param stringExpansionMapper The String to Expansion Mapper
   * @param gameBoardMapper       The GameBard Mapper
   * @param gsonInstance          The GSON we will be using
   * @param lsLocation            The LobbyService Location
   */
  public GameHandlerController(@Autowired Mapper<User, Player> userPlayerMapper,
                               @Autowired Mapper<String, Expansion> stringExpansionMapper,
                               @Autowired Mapper<GameBoard, SentGameBoard> gameBoardMapper,
                               @Autowired GsonInstance gsonInstance,
                               @Value("${ls.location}") String lsLocation) {
    this.userPlayerMapper = userPlayerMapper;
    this.stringExpansionMapper = stringExpansionMapper;
    this.gameBoardMapper = gameBoardMapper;
    this.gsonInstance = gsonInstance;
    this.lsLocation = lsLocation;
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
    gameManager.put(
        Arrays.stream(launchGameForm.players()).map(User::name).collect(Collectors.toSet()),
        createGame(gameid, launchGameForm)); // TODO add checks
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  private GameBoard createGame(String gameid, LaunchGameForm launchGameForm) {
    Set<Noble> nobles = null; // TODO

    Set<Expansion> expansions = Set.of(stringExpansionMapper.map(launchGameForm.gameType()));
    Set<Player> players = Arrays.stream(launchGameForm.players()).map(userPlayerMapper::map)
        .collect(Collectors.toSet());
    String creator = launchGameForm.creator();

    return new GameBoard(nobles, expansions, players, gameid, creator);
  }

  /**
   * Remove a game with a matching game ID.
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
    for (Map.Entry<Set<String>, GameBoard> entry : gameManager.entrySet()) {
      if (entry.getValue().gameid().equals(gameid)) {
        gameManager.remove(entry.getKey());
        return true;
      }
    }
    return false;
  }

  private GameBoard getGame(String username) {
    for (Map.Entry<Set<String>, GameBoard> entry : gameManager.entrySet()) {
      if (entry.getKey().contains(username)) {
        return entry.getValue();
      }
    }
    return null;
  }

  private String getUsername(String accessToken) {
    return Unirest.post("%soauth/username".formatted(lsLocation))
        .queryString("access_token", accessToken).asString().getBody();
  }

  /**
   * Get a game board.
   *
   * @param accessToken The user's access token who is sending the request
   * @return The full response
   */
  @GetMapping(value = "board", produces = "application/json; charset=utf-8")
  public ResponseEntity<String> retrieveGame(@RequestParam("access_token") String accessToken) {
    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(username);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    } else {
      final SentGameBoard sentGameBoard = gameBoardMapper.map(gameBoard);
      return ResponseEntity.status(HttpStatus.OK)
          .body(gsonInstance.gson.toJson(sentGameBoard, SentGameBoard.class));
    }
  }

  /**
   * Purchase a given card.
   *
   * @param accessToken      The access token belonging to the player trying to purchase the card.
   * @param purchaseCardForm The purchase card form.
   * @return The response.
   */
  @PostMapping(value = "card", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> purchaseCard(@RequestParam("access_token") String accessToken,
                                             @RequestBody PurchaseCardForm purchaseCardForm) {
    // TODO check what type of card it is and add the prestige points, gem discounts, etc
    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(username);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    }

    final Hand hand = getHand(gameBoard.players(), username);
    if (hand == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    final Card card = purchaseCardForm.card();
    final Set<List<Card>> decks = gameBoard.cards();
    if (!cardIsFaceUpOnBoard(decks, card)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("card chosen for purchase is not valid");
    }

    final Gems substitutedGems = purchaseCardForm.substitutedGems();
    if (substitutedGems.containsKey(GemColor.GOLD)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot substitute gold gems for gold gems");
    }

    final Gems gemsToPayWith = purchaseCardForm.gemsToPayWith();
    if (countGemAmount(substitutedGems) != gemsToPayWith.get(GemColor.GOLD)) {
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
    // Add the card to the player's hand
    hand.purchasedCards().add(card);
    // Take gems from player
    removeGems(ownedGems, gemsToPayWith);
    // Add gems to bank
    addGems(gameBoard.availableGems(), gemsToPayWith);

    return ResponseEntity.status(HttpStatus.OK).body(null);
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
    gemsToRemove.forEach((key, amountToRemove) -> {
      final int amount = gemsToRemoveFrom.get(key);
      final int newValue = amount - amountToRemove;
      if (newValue == 0) {
        gemsToRemoveFrom.remove(key);
      } else {
        gemsToRemoveFrom.put(key, newValue);
      }
    });
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
    return gemsToPayWith.entrySet().stream().noneMatch(
        entry -> !ownedGems.containsKey(entry.getKey())
                 || ownedGems.get(entry.getKey()) < entry.getValue());
  }

  private Hand getHand(Collection<Player> players, String username) {
    return players.stream().filter(p -> p.uid().equals(username)).findFirst().map(Player::hand)
        .orElse(null);
  }
}