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
  private int gemDiscount;
  private GemColor discountColor;
  private Noble nobleToReserve;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param gemDiscount    the amount of gems to be discounted
   * @param discountColor  the color of the gems to be discounted
   * @param nobleToReserve the noble to be reserved
   */
  public ReserveNobleCard(int prestigePoints, Gems cost,
                          CardLevel level,
                          Expansion expansion, int gemDiscount, GemColor discountColor,
                          Noble nobleToReserve) {
    super(prestigePoints, cost, level, expansion);
    this.gemDiscount = gemDiscount;
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
   * @param gemDiscount    the amount of gems to be discounted
   * @param discountColor  the color of the gems to be discounted
   */
  public ReserveNobleCard(int prestigePoints, Gems cost,
                          CardLevel level,
                          Expansion expansion, int gemDiscount, GemColor discountColor) {
    this(prestigePoints, cost, level, expansion, gemDiscount, discountColor, null);
  }

  /**
   * No args constructor.
   */
  public ReserveNobleCard() {
    super();
  }

  /**
   * A Getter for the Gem Discount.
   *
   * @return Gem Discount
   */
  public int gemDiscount() {
    return gemDiscount;
  }

  /**
   * A Getter for the Color of the Gem which is allowing a Discount.
   *
   * @return discount color
   */
  public GemColor discountColor() {
    return discountColor;
  }

  public Noble nobleToReserve() {
    return nobleToReserve;
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
           && gemDiscount == card.gemDiscount && discountColor == card.discountColor
           && Objects.equals(nobleToReserve, card.nobleToReserve);
  }
}
