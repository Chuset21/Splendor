package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.control.form.tradingpost.TradingPostTakeGem;
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
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Game board helper, containing static helper methods.
 */
public final class GameBoardHelper {
  /**
   * Private no args constructor to stop instantiation.
   */
  private GameBoardHelper() {

  }

  /**
   * Purchase a satchel card.
   *
   * @param card      card to purchase
   * @param hand      hand
   * @param gameBoard game board
   * @return null if successful, response containing the error message otherwise
   */
  public static ResponseEntity<String> purchaseSatchelCard(SatchelCard card, Hand hand,
                                                           GameBoard gameBoard) {
    if (card.cardToAttach() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("card to attach cannot be null");
    }

    CardLevel level = card.level();
    if (Objects.requireNonNull(level) == CardLevel.ONE) {
      if (card.freeCardToTake() != null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("level one satchel card does not allow you to take a free card");
      }
    } else if (level == CardLevel.TWO) {
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
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("satchel card configured to the wrong level");
    }
    // Now must add extra gem discount
    final ResponseEntity<String> response = addAttachedCardGemDiscounts(card, hand);
    if (response != null) {
      return response;
    }
    card.removeFreeCardToTake();
    // The card is now attached to the satchel card
    hand.purchasedCards().remove(card.cardToAttach());
    return null;
  }

  private static ResponseEntity<String> addAttachedCardGemDiscounts(SatchelCard card, Hand hand) {
    if (!hand.purchasedCards().contains(card.cardToAttach())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot attach a satchel card to a card that you don't own");
    }

    if (Objects.requireNonNull(card.cardToAttach()) instanceof StandardCard c) {
      hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
    } else if (card.cardToAttach() instanceof DoubleBonusCard c) {
      hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
    } else if (card.cardToAttach() instanceof ReserveNobleCard c) {
      hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
    } else if (card.cardToAttach() instanceof WaterfallCard c) {
      hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
    } else if (card.cardToAttach() instanceof SacrificeCard c) {
      hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot attach a card that has no gem discount to a satchel card");
    }
    return null;
  }

  /**
   * Reserve a noble from a reserve noble card.
   *
   * @param gameBoard game board
   * @param hand      hand
   * @param card      the reserve noble card
   * @return null if successful, response containing the error message otherwise
   */
  public static ResponseEntity<String> reserveNoble(GameBoard gameBoard, Hand hand,
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
    hand.gemDiscounts().merge(card.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
    // Add reserved noble to hand and remove noble from game board
    if (card.nobleToReserve() != null) {
      hand.reservedNobles().add(card.nobleToReserve());
      gameBoard.availableNobles().remove(card.nobleToReserve());
    }
    card.removeNobleToReserve();
    return null;
  }

  /**
   * Purchase a free card from either a satchel card or waterfall card.
   *
   * @param card      the card to purchase for free
   * @param hand      hand
   * @param gameBoard game board
   * @return null if successful, response containing the error message otherwise
   */
  public static ResponseEntity<String> purchaseFreeCard(Card card, Hand hand,
                                                        GameBoard gameBoard) {
    if (!isCardFaceUpOnBoard(gameBoard.cards(), card)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("free card to take is not available");
    }

    if (Objects.requireNonNull(card) instanceof DoubleBonusCard c) {
      hand.gemDiscounts().merge(c.discountColor(), Bonus.DOUBLE.getValue(), Integer::sum);
    } else if (card instanceof StandardCard c) {
      hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
    } else if (card instanceof ReserveNobleCard c) {
      final ResponseEntity<String> response = reserveNoble(gameBoard, hand, c);
      if (response != null) {
        return response;
      }
    } else if (card instanceof SatchelCard c) {
      final ResponseEntity<String> response = purchaseSatchelCard(c, hand, gameBoard);
      if (response != null) {
        return response;
      }
    } else if (!(card instanceof GoldGemCard)) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("waterfall card or sacrifice card configured to the wrong level");
    }
    // Increment the player's prestige points
    hand.incrementPrestigePoints(card.prestigePoints());
    // Add the card to the player's hand
    hand.purchasedCards().add(card);
    // Remove the card from the deck
    removeCardFromDeck(gameBoard.cards(), card);
    return null;
  }

  /**
   * Remove a number of gold gem cards from a player's hand.
   *
   * @param numGoldGemCards the number of gold gem cards to remove
   * @param hand            the player's hand
   */
  public static void removeGoldGemCards(int numGoldGemCards, Hand hand) {
    for (int i = 0; numGoldGemCards > 0; ) {
      if (hand.purchasedCards().get(i) instanceof GoldGemCard) {
        hand.purchasedCards().remove(i);
        numGoldGemCards--;
      } else {
        i++;
      }
    }
  }

  /**
   * Decrement the gem discounts that this card gives to the player.
   *
   * @param card the card to containing the gem discount
   * @param hand hand
   * @return true if successful, false otherwise
   */
  public static boolean decrementGemDiscounts(Card card, Hand hand) {
    if (Objects.requireNonNull(card) instanceof ReserveNobleCard c) {
      hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == Bonus.SINGLE.getValue() ? null : v - Bonus.SINGLE.getValue());
    } else if (card instanceof DoubleBonusCard c) {
      hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == Bonus.DOUBLE.getValue() ? null : v - Bonus.DOUBLE.getValue());
    } else if (card instanceof SacrificeCard c) {
      hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == Bonus.SINGLE.getValue() ? null : v - Bonus.SINGLE.getValue());
    } else if (card instanceof StandardCard c) {
      hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == Bonus.SINGLE.getValue() ? null : v - Bonus.SINGLE.getValue());
    } else if (card instanceof WaterfallCard c) {
      hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == Bonus.SINGLE.getValue() ? null : v - Bonus.SINGLE.getValue());
    } else {
      return false;
    }
    return true;
  }

  /**
   * Whether the card matches the sacrifice card's discount color.
   *
   * @param sacrificeCard sacrifice card
   * @param card          card to sacrifice
   * @return true if it does match the discount color, false otherwise
   */
  public static boolean matchesCardDiscountColor(SacrificeCard sacrificeCard,
                                                 Card card) {
    if (Objects.requireNonNull(card) instanceof ReserveNobleCard c) {
      return c.discountColor() == sacrificeCard.sacrificeColor();
    } else if (card instanceof DoubleBonusCard c) {
      return c.discountColor() == sacrificeCard.sacrificeColor();
    } else if (card instanceof SacrificeCard c) {
      return c.discountColor() == sacrificeCard.sacrificeColor();
    } else if (card instanceof StandardCard c) {
      return c.discountColor() == sacrificeCard.sacrificeColor();
    } else if (card instanceof WaterfallCard c) {
      return c.discountColor() == sacrificeCard.sacrificeColor();
    }
    return false;
  }

  /**
   * Has a side effect of removing the face down card from the deck if successful.
   *
   * @param decks the decks
   * @param card  the card to get
   * @return the card that was removed from the deck if successful, null otherwise
   */
  public static Card getFaceDownCard(Set<List<Card>> decks, Card card) {
    for (List<Card> deck : decks) {
      if (!deck.isEmpty()) {
        final Card firstCard = deck.get(0);
        if (firstCard.level() == card.level() && firstCard.expansion() == card.expansion()) {
          final int cardIndex = firstCard.expansion() == Expansion.STANDARD ? 4 : 2;
          return deck.size() <= cardIndex ? null : deck.remove(cardIndex);
        }
      }
    }
    return null;
  }

  /**
   * Get the discounted cost from a card's cost.
   *
   * @param originalCost the card's cost
   * @param gemDiscounts the player's gem discounts
   * @return The discounted cost
   */
  public static Gems getDiscountedCost(Gems originalCost, Gems gemDiscounts) {
    final Gems result = new Gems(originalCost);

    // Remove the gem discounts from the cost of the card to get the cost for this specific player
    removeGems(result, gemDiscounts);

    return result;
  }

  /**
   * Add gems to other gems.
   *
   * @param gemsToAddTo the gems to add to
   * @param gemsToAdd   the gems to add
   */
  public static void addGems(Gems gemsToAddTo, Gems gemsToAdd) {
    gemsToAdd.forEach((key, value) -> gemsToAddTo.merge(key, value, Integer::sum));
  }

  /**
   * Remove gems from other gems.
   *
   * @param gemsToRemoveFrom the gems to remove from
   * @param gemsToRemove     the gems to remove
   */
  public static void removeGems(Gems gemsToRemoveFrom, Gems gemsToRemove) {
    gemsToRemove.forEach((key, amountToRemove) -> gemsToRemoveFrom.computeIfPresent(key,
        (k, v) -> amountToRemove >= v ? null : v - amountToRemove));
  }

  /**
   * Remove a card from a deck.
   *
   * @param decks decks
   * @param card  card to remove from the deck
   * @return true if successful, false otherwise.
   */
  public static boolean removeCardFromDeck(Set<List<Card>> decks, Card card) {
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

  /**
   * Determine whether a player's payment is enough to afford paying for this resource.
   *
   * @param requiredGems       The required gems, the resource's cost
   * @param paymentWithoutGold The player's payment excluding gold gems
   * @param goldGemsUsed       The number of gold gems used, these act like wild tokens
   * @param goldGemsMultiplier The amount of gems each gold gem is worth.
   * @return The response containing the error message if there was an error, null otherwise.
   */
  public static ResponseEntity<String> canAffordPayment(Gems requiredGems, Gems paymentWithoutGold,
                                                        int goldGemsUsed, int goldGemsMultiplier) {
    final Gems requiredCopy = new Gems(requiredGems);
    removeGems(requiredCopy, paymentWithoutGold);
    if (requiredGems.count() != paymentWithoutGold.count() + requiredCopy.count()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("gems used to pay don't match up to cost");
    }

    if (requiredCopy.count() == 0) {
      return null;
    }

    for (final int val : requiredCopy.values()) {
      // Ceiling division
      final int numberOfGemsUsed = (val + goldGemsMultiplier - 1) / goldGemsMultiplier;
      goldGemsUsed -= numberOfGemsUsed;
    }
    if (goldGemsUsed < 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not enough gold gems used");
    }
    return null;
  }

  /**
   * Returns whether a card is face up on the board or not.
   *
   * @param decks the decks of cards
   * @param card  the card to check for
   * @return true if the card is face up on the board, false otherwise
   */
  public static boolean isCardFaceUpOnBoard(Set<List<Card>> decks, Card card) {
    for (List<Card> cards : decks) {
      if (!cards.isEmpty()) {
        final Card firstCard = cards.get(0);
        if (firstCard.level() == card.level() && firstCard.expansion() == card.expansion()) {
          final int limit =
              Integer.min(cards.size(), firstCard.expansion() == Expansion.STANDARD ? 4 : 2);
          return cards.subList(0, limit).stream().anyMatch(card::equals);
        }
      }
    }
    return false;
  }

  /**
   * Get a player's hand if their username matches.
   *
   * @param players  a collection of players
   * @param username the player's username
   * @return the player's hand if successful, null otherwise.
   */
  public static Hand getHand(Collection<Player> players, String username) {
    return players.stream().filter(p -> p.uid().equals(username)).findFirst().map(Player::hand)
        .orElse(null);
  }

  /**
   * Determine whether a player has at least a number of gems of one single color.
   *
   * @param amount The amount of gems they must have of a single color
   * @param gems   The player's gems
   * @return True if the player has enough gems of a single color, false otherwise.
   */
  public static boolean hasAmountOfSingleGem(int amount, Gems gems) {
    return gems.values().stream().anyMatch(c -> c >= amount);
  }

  /**
   * Determine whether a player has enough gem discounts to acquire a city.
   *
   * @param gemDiscounts      The player's gem discounts.
   * @param requiredDiscounts The required discounts to acquire the city.
   * @return True if the player has enough gem discounts, false otherwise.
   */
  public static boolean hasEnoughDiscountsForCity(Gems gemDiscounts, Gems requiredDiscounts) {
    if (requiredDiscounts.containsKey(GemColor.GOLD)) {
      final Gems requiredWithoutGold = new Gems(requiredDiscounts);
      requiredWithoutGold.remove(GemColor.GOLD);
      final Gems gemDiscountsWithoutRequired = new Gems(gemDiscounts);
      requiredWithoutGold.keySet().forEach(gemDiscountsWithoutRequired::remove);
      return gemDiscounts.hasEnoughGems(requiredWithoutGold)
             && hasAmountOfSingleGem(requiredDiscounts.get(GemColor.GOLD),
          gemDiscountsWithoutRequired);
    } else {
      return gemDiscounts.hasEnoughGems(requiredDiscounts);
    }
  }

  /**
   * Execute trading post one power if it is owned.
   *
   * @param gameBoard          Game board.
   * @param hand               The player's hand.
   * @param tradingPostTakeGem Trading post take gem.
   */
  public static void executeTradingPostOnePower(GameBoard gameBoard, Hand hand,
                                                TradingPostTakeGem tradingPostTakeGem) {
    if (hand.tradingPosts().getOrDefault(TradingPostsEnum.BONUS_GEM_WITH_CARD, false)
        && tradingPostTakeGem.gemToTake() != null) {
      hand.gems().merge(tradingPostTakeGem.gemToTake(), 1, Integer::sum);
      gameBoard.availableGems()
          .computeIfPresent(tradingPostTakeGem.gemToTake(), (k, v) -> 1 >= v ? null : v - 1);
      if (tradingPostTakeGem.gemToRemove() != null) {
        hand.gems()
            .computeIfPresent(tradingPostTakeGem.gemToRemove(), (k, v) -> 1 >= v ? null : v - 1);
        gameBoard.availableGems().merge(tradingPostTakeGem.gemToRemove(), 1, Integer::sum);
      }
    }
  }
}
