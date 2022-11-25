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
  private Gems gems;
  private Set<Card> reservedCards;
  private Set<Card> purchasedCards;
  private Noble visitedNoble;
  private Noble reservedNoble;
  private Gems gemDiscounts;

  /**
   * Constructor.
   *
   * @param gems           the amount of gems the owned
   * @param reservedCards  the reserved cards
   * @param purchasedCards the purchased cards
   * @param visitedNoble   the owned noble
   * @param reservedNoble  the reserved noble
   * @param gemDiscounts   the gem discounts
   */
  public Hand(Gems gems, Set<Card> reservedCards, Set<Card> purchasedCards, Noble visitedNoble,
              Noble reservedNoble, Gems gemDiscounts) {
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
    gems = new Gems();
    Arrays.stream(GemColor.values()).forEach(e -> gems.put(e, 3));
    reservedCards = new HashSet<>();
    purchasedCards = new HashSet<>();
    visitedNoble = null;
    reservedNoble = null;
    gemDiscounts = new Gems();
  }

  public Gems gems() {
    return gems;
  }

  public Set<Card> reservedCards() {
    return reservedCards;
  }

  public Set<Card> purchasedCards() {
    return purchasedCards;
  }

  public Noble visitedNoble() {
    return visitedNoble;
  }

  public Noble reservedNoble() {
    return reservedNoble;
  }

  public Gems gemDiscounts() {
    return gemDiscounts;
  }

  public void setGems(Gems gems) {
    this.gems = gems;
  }

  public void setReservedCards(Set<Card> reservedCards) {
    this.reservedCards = reservedCards;
  }

  public void setPurchasedCards(Set<Card> purchasedCards) {
    this.purchasedCards = purchasedCards;
  }

  public void setVisitedNoble(Noble visitedNoble) {
    this.visitedNoble = visitedNoble;
  }

  public void setReservedNoble(Noble reservedNoble) {
    this.reservedNoble = reservedNoble;
  }

  public void setGemDiscounts(Gems gemDiscounts) {
    this.gemDiscounts = gemDiscounts;
  }
}
