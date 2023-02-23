package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.tradingposts.TradingPosts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A player's hand.
 */
public final class Hand {
  private int prestigePoints;
  private Gems gems;
  private final List<Card> reservedCards;
  private final List<Card> purchasedCards;
  private final Set<Noble> visitedNobles;
  private final Set<Noble> reservedNobles;
  private Gems gemDiscounts;
  private TradingPosts tradingPosts;

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
   * @param tradingPosts   the trading post powers a playes has
   */
  public Hand(int prestigePoints, Gems gems, List<Card> reservedCards, List<Card> purchasedCards,
              Set<Noble> visitedNobles, Set<Noble> reservedNobles, Gems gemDiscounts,
              TradingPosts tradingPosts) {
    this.prestigePoints = prestigePoints;
    this.gems = gems;
    this.reservedCards = reservedCards;
    this.purchasedCards = purchasedCards;
    this.visitedNobles = visitedNobles;
    this.reservedNobles = reservedNobles;
    this.gemDiscounts = gemDiscounts;
    this.tradingPosts = tradingPosts;
  }

  /**
   * Default constructor.
   * For the demo we start with 3 of each gem.
   */
  public Hand() {
    prestigePoints = 0;
    gems = new Gems();
    Arrays.stream(GemColor.values()).forEach(e -> gems.put(e, 3));
    reservedCards = new ArrayList<>();
    purchasedCards = new ArrayList<>();
    visitedNobles = new HashSet<>();
    reservedNobles = new HashSet<>();
    gemDiscounts = new Gems();
    tradingPosts = new TradingPosts();
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
   * A Getter for the Trading Post powers a Player currently has.
   *
   * @return The Trading Post powers a Player currently has.
   */
  public TradingPosts tradingPosts() {
    return tradingPosts;
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
   * A Setter for the Gem Discounts.
   *
   * @param gemDiscounts The Gem Discounts that have been allocated to set
   */
  public void setGemDiscounts(Gems gemDiscounts) {
    this.gemDiscounts = gemDiscounts;
  }

  /**
   * a Setter for the Trading Posts.
   *
   * @param tradingPosts The Trading Posts that have been allocated to set
   */
  public void setTradingPosts(TradingPosts tradingPosts) {
    this.tradingPosts = tradingPosts;
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
