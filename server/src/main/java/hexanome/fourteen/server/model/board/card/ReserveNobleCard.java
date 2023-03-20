package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.Noble;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.Objects;

/**
 * Reserve Noble Card.
 */
public final class ReserveNobleCard extends Card {
  private GemColor discountColor;
  private Noble nobleToReserve;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param discountColor  the color of the gems to be discounted
   * @param nobleToReserve the noble to be reserved
   */
  public ReserveNobleCard(int prestigePoints, Gems cost,
                          CardLevel level,
                          Expansion expansion, GemColor discountColor, Noble nobleToReserve) {
    super(prestigePoints, cost, level, expansion);
    this.discountColor = discountColor;
    this.nobleToReserve = nobleToReserve;
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
  public ReserveNobleCard(int prestigePoints, Gems cost,
                          CardLevel level, Expansion expansion, GemColor discountColor) {
    this(prestigePoints, cost, level, expansion, discountColor, null);
  }

  /**
   * No args constructor.
   */
  public ReserveNobleCard() {
    super();
  }

  /**
   * A Getter for the Color of the Gem which is allowing a Discount.
   *
   * @return discount color
   */
  public GemColor discountColor() {
    return discountColor;
  }

  /**
   * Getter for noble to reserve.
   *
   * @return The noble to reserve.
   */
  public Noble nobleToReserve() {
    return nobleToReserve;
  }

  /**
   * Remove the noble to reserve from this card.
   * This is intended to be used after successfully reserving a noble.
   */
  public void removeNobleToReserve() {
    nobleToReserve = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ReserveNobleCard card = (ReserveNobleCard) obj;
    return super.prestigePoints == card.prestigePoints && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion
           && discountColor == card.discountColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost, level, expansion, discountColor);
  }
}
