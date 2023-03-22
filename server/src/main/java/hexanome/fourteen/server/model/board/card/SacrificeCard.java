package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.Objects;

/**
 * Sacrifice Card.
 */
public final class SacrificeCard extends Card {
  private GemColor discountColor;
  private GemColor sacrificeColor;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card, this should be empty or null
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param discountColor  the color of the gems to be discounted
   * @param sacrificeColor the color of the cards to sacrifice
   */
  public SacrificeCard(int prestigePoints, Gems cost,
                       CardLevel level, Expansion expansion, GemColor discountColor,
                       GemColor sacrificeColor) {
    super(prestigePoints, cost, level, expansion);
    this.discountColor = discountColor;
    this.sacrificeColor = sacrificeColor;
  }

  /**
   * No args constructor.
   */
  public SacrificeCard() {
    super();
  }

  /**
   * A Getter for the Discount Color associated with the Sacrifice Card.
   *
   * @return The Discount Color.
   */
  public GemColor discountColor() {
    return discountColor;
  }

  /**
   * A Getter for the sacrifice Color associated with the Sacrifice Card.
   *
   * @return The sacrifice Color.
   */
  public GemColor sacrificeColor() {
    return sacrificeColor;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    SacrificeCard card = (SacrificeCard) obj;
    return super.prestigePoints == card.prestigePoints && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && discountColor == card.discountColor && sacrificeColor == card.sacrificeColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost, level, expansion, discountColor, sacrificeColor);
  }
}
