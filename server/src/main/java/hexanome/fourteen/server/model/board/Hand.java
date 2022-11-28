package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A player's hand.
 */
public final class Hand {
  private int prestigePoints;
  private Gems gems;
  private Set<Card> reservedCards;
  private Set<Card> purchasedCards;
  private Noble visitedNoble;
  private Noble reservedNoble;
  private Gems gemDiscounts;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points the player has
   * @param gems           the amount of gems the owned
   * @param reservedCards  the reserved cards
   * @param purchasedCards the purchased cards
   * @param visitedNoble   the owned noble
   * @param reservedNoble  the reserved noble
   * @param gemDiscounts   the gem discounts
   */
  public Hand(int prestigePoints, Gems gems, Set<Card> reservedCards, Set<Card> purchasedCards,
              Noble visitedNoble,
              Noble reservedNoble, Gems gemDiscounts) {
    this.prestigePoints = prestigePoints;
    this.gems = gems;
    this.reservedCards = reservedCards;
    this.purchasedCards = purchasedCards;
    this.visitedNoble = visitedNoble;
    this.reservedNoble = reservedNoble;
    this.gemDiscounts = gemDiscounts;
  }

  /**
   * Default constructor.
   * For the demo we start with 3 of each gem.
   */
  public Hand() {
    prestigePoints = 0;
    gems = new Gems();
    Arrays.stream(GemColor.values()).forEach(e -> gems.put(e, 3));
    reservedCards = new HashSet<>();
    purchasedCards = new HashSet<>();
    visitedNoble = null;
    reservedNoble = null;
    gemDiscounts = new Gems();
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
  public Set<Card> reservedCards() {
    return reservedCards;
  }

  /**
   * A Getter for the Purchased Cards.
   *
   * @return Cards purchased.
   */
  public Set<Card> purchasedCards() {
    return purchasedCards;
  }

  /**
   * A Getter for the Noble that is visiting.
   *
   * @return The Noble that is visiting.
   */
  public Noble visitedNoble() {
    return visitedNoble;
  }

  /**
   * A Getter for the Noble that has been reserved.
   *
   * @return The Noble that has been reserved.
   */
  public Noble reservedNoble() {
    return reservedNoble;
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
   * A Setter for the Gems.
   *
   * @param gems Gems to set
   */
  public void setGems(Gems gems) {
    this.gems = gems;
  }

  /**
   * A Setter for the Reserved Cards.
   *
   * @param reservedCards Cards that have been reserved to set
   */
  public void setReservedCards(Set<Card> reservedCards) {
    this.reservedCards = reservedCards;
  }

  /**
   * A Setter for the Purchased Cards.
   *
   * @param purchasedCards Cards that have been purchased to set
   */
  public void setPurchasedCards(Set<Card> purchasedCards) {
    this.purchasedCards = purchasedCards;
  }

  /**
   * A Setter for the Visited Noble.
   *
   * @param visitedNoble The Visiting Noble to set
   */
  public void setVisitedNoble(Noble visitedNoble) {
    this.visitedNoble = visitedNoble;
  }

  /**
   * A Setter for the Reserved Noble.
   *
   * @param reservedNoble The Visiting Noble that has been reserved to set
   */
  public void setReservedNoble(Noble reservedNoble) {
    this.reservedNoble = reservedNoble;
  }

  /**
   * A Setter for the Gem Discounts.
   *
   * @param gemDiscounts The Gem Discounts that have been allocated to set
   */
  public void setGemDiscounts(Gems gemDiscounts) {
    this.gemDiscounts = gemDiscounts;
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
}
