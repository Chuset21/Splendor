package hexanome.fourteen.server.model.board;

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
import java.util.Collection;
import java.util.List;
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
    // The card is now attached to the satchel card
    hand.purchasedCards().remove(card.cardToAttach());
    return null;
  }

  private static ResponseEntity<String> addAttachedCardGemDiscounts(SatchelCard card, Hand hand) {
    if (!hand.purchasedCards().contains(card.cardToAttach())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("cannot attach a satchel card to a card that you don't own");
    }

    switch (card.cardToAttach()) {
      case StandardCard c ->
          hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
      case DoubleBonusCard c ->
          hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
      case ReserveNobleCard c ->
          hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
      case default -> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("cannot attach a card that has no gem discount to a satchel card");
      }
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
    // Add reserved noble to hand
    if (card.nobleToReserve() != null) {
      hand.reservedNobles().add(card.nobleToReserve());
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

    switch (card) {
      case DoubleBonusCard c ->
          hand.gemDiscounts().merge(c.discountColor(), Bonus.DOUBLE.getValue(), Integer::sum);
      case StandardCard c ->
          hand.gemDiscounts().merge(c.discountColor(), Bonus.SINGLE.getValue(), Integer::sum);
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
    switch (card) {
      case ReserveNobleCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == Bonus.SINGLE.getValue() ? null : v - Bonus.SINGLE.getValue());
      case DoubleBonusCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == Bonus.DOUBLE.getValue() ? null : v - Bonus.DOUBLE.getValue());
      case SacrificeCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == Bonus.SINGLE.getValue() ? null : v - Bonus.SINGLE.getValue());
      case StandardCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == Bonus.SINGLE.getValue() ? null : v - Bonus.SINGLE.getValue());
      case WaterfallCard c -> hand.gemDiscounts().computeIfPresent(c.discountColor(),
          (k, v) -> v == Bonus.SINGLE.getValue() ? null : v - Bonus.SINGLE.getValue());
      case default -> {
        return false;
      }
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
   * Has a side effect of removing the face down card from the deck if successful.
   */
  public static Card getFaceDownCard(Set<List<Card>> decks, Card card) {
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
   */
  public static void addGems(Gems gemsToAddTo, Gems gemsToAdd) {
    gemsToAdd.forEach((key, value) -> gemsToAddTo.merge(key, value, Integer::sum));
  }

  /**
   * Remove gems from other gems.
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
   * Get payment without gold gems.
   *
   * @param substitutedGems the gems substituted for gold gems
   * @param gemsToPayWith   the gems the player plans to pay with
   * @return the gemsToPayWith with the gold gems being substituted
   */
  public static Gems getPaymentWithoutGoldGems(Gems substitutedGems, Gems gemsToPayWith) {
    final Gems result = new Gems(gemsToPayWith);
    result.remove(GemColor.GOLD);

    substitutedGems.forEach((key, value) -> result.merge(key, value, Integer::sum));

    return result;
  }

  /**
   * Returns whether a card is face up on the board or not.
   */
  public static boolean isCardFaceUpOnBoard(Set<List<Card>> decks, Card card) {
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

  /**
   * Count the total amount of gems in a gems object.
   */
  public static int countGemAmount(Gems gems) {
    return gems.values().stream().mapToInt(value -> value).sum();
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
}
