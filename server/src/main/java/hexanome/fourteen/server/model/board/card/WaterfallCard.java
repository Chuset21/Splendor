package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.Objects;

/**
 * Waterfall Card.
 */
public final class WaterfallCard extends Card {
  private GemColor discountColor;
  private Card cardToTake;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param discountColor  the color of the gems to be discounted
   * @param cardToTake     the card to take for free
   */
  public WaterfallCard(int prestigePoints, Gems cost, CardLevel level, Expansion expansion,
                       GemColor discountColor, Card cardToTake) {
    super(prestigePoints, cost, level, expansion);
    this.discountColor = discountColor;
    this.cardToTake = cardToTake;
  }

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param discountColor  the color of the gems to be discounted
   */
  public WaterfallCard(int prestigePoints, Gems cost, CardLevel level, Expansion expansion,
                       GemColor discountColor) {
    this(prestigePoints, cost, level, expansion, discountColor, null);
  }

  /**
   * No args constructor.
   */
  public WaterfallCard() {
    super();
  }

  /**
   * A Getter for the Discount Color of a WaterfallCard.
   *
   * @return The Discount Color
   */
  public GemColor discountColor() {
    return discountColor;
  }

  /**
   * Getter for card to take.
   *
   * @return The card to take.
   */
  public Card cardToTake() {
    return cardToTake;
  }

  /**
   * Remove the card to take from this card.
   * This is intended to be used after successfully taking a free card.
   */
  public void removeCardToTake() {
    cardToTake = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    WaterfallCard card = (WaterfallCard) obj;
    return super.prestigePoints == card.prestigePoints && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && discountColor == card.discountColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost, level, expansion, discountColor);
  }
}
