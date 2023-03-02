package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A player's hand.
 */
public final class Hand {
  private int prestigePoints;
  private final Gems gems;
  private final List<Card> reservedCards;
  private final List<Card> purchasedCards;
  private final Set<Noble> visitedNobles;
  private final Set<Noble> reservedNobles;
  private final Gems gemDiscounts;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points the player has
   * @param gems           the amount of gems the owned
   * @param reservedCards  the reserved cards
   * @param purchasedCards the purchased cards
   * @param visitedNobles  the owned nobles
   * @param reservedNobles the reserved nobles
   * @param gemDiscounts   the gem discounts
   */
  public Hand(int prestigePoints, Gems gems, List<Card> reservedCards, List<Card> purchasedCards,
              Set<Noble> visitedNobles, Set<Noble> reservedNobles, Gems gemDiscounts) {
    this.prestigePoints = prestigePoints;
    this.gems = gems;
    this.reservedCards = reservedCards;
    this.purchasedCards = purchasedCards;
    this.visitedNobles = visitedNobles;
    this.reservedNobles = reservedNobles;
    this.gemDiscounts = gemDiscounts;
  }

  /**
   * Default constructor.
   */
  public Hand() {
    this(0, new Gems(), new ArrayList<>(), new ArrayList<>(), new HashSet<>(), new HashSet<>(),
        new Gems());
  }

  /**
   * A Getter for the prestige points.
   *
   * @return Player's prestige points
   */
  public int prestigePoints() {
    return prestigePoints;
  }

  /**
   * A Getter for the Gems.
   *
   * @return Gems owned.
   */
  public Gems gems() {
    return gems;
  }

  /**
   * A Getter for the Reserved Cards.
   *
   * @return Cards reserved.
   */
  public List<Card> reservedCards() {
    return reservedCards;
  }

  /**
   * A Getter for the Purchased Cards.
   *
   * @return Cards purchased.
   */
  public List<Card> purchasedCards() {
    return purchasedCards;
  }

  /**
   * A Getter for the Nobles that are visiting.
   *
   * @return The Nobles that are visiting.
   */
  public Set<Noble> visitedNobles() {
    return visitedNobles;
  }

  /**
   * A Getter for the Nobles that have been reserved.
   *
   * @return The Nobles that have been reserved.
   */
  public Set<Noble> reservedNobles() {
    return reservedNobles;
  }

  /**
   * A Getter for the Gem Discounts the Player currently has.
   *
   * @return The Gem Discounts the Player currently has.
   */
  public Gems gemDiscounts() {
    return gemDiscounts;
  }

  /**
   * A Setter for the prestige points.
   *
   * @param prestigePoints prestige points to set
   */
  public void setPrestigePoints(int prestigePoints) {
    this.prestigePoints = prestigePoints;
  }

  /**
   * Increment prestige points.
   *
   * @param prestigePoints prestige points to add
   */
  public void incrementPrestigePoints(int prestigePoints) {
    this.prestigePoints += prestigePoints;
  }

  /**
   * Decrement prestige points.
   *
   * @param prestigePoints prestige points to remove
   */
  public void decrementPrestigePoints(int prestigePoints) {
    this.prestigePoints -= prestigePoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Hand hand = (Hand) o;
    return prestigePoints == hand.prestigePoints && Objects.equals(gems, hand.gems)
           && Objects.equals(reservedCards, hand.reservedCards)
           && Objects.equals(purchasedCards, hand.purchasedCards)
           && Objects.equals(visitedNobles, hand.visitedNobles)
           && Objects.equals(reservedNobles, hand.reservedNobles)
           && Objects.equals(gemDiscounts, hand.gemDiscounts);
  }
}
