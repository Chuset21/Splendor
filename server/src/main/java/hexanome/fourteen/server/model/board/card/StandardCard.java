package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.Objects;

/**
 * Standard Card.
 */
public final class StandardCard extends Card {
  private GemColor discountColor;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param discountColor  the color of the gems to be discounted
   */
  public StandardCard(int prestigePoints, Gems cost, CardLevel level, Expansion expansion,
                      GemColor discountColor) {
    super(prestigePoints, cost, level, expansion);
    this.discountColor = discountColor;
  }

  /**
   * No args constructor.
   */
  public StandardCard() {
    super();
  }

  /**
   * A Getter for the Discount Color associated with the StandardCard.
   *
   * @return The Discount Color
   */
  public GemColor discountColor() {
    return discountColor;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    StandardCard card = (StandardCard) obj;
    return super.prestigePoints == card.prestigePoints && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && discountColor == card.discountColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost, level, expansion, discountColor);
  }
}