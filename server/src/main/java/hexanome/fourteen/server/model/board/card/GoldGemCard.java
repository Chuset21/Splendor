package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.Objects;

/**
 * Gold Gem Card.
 */
public final class GoldGemCard extends Card {

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   */
  public GoldGemCard(int prestigePoints, Gems cost, CardLevel level, Expansion expansion) {
    super(prestigePoints, cost, level, expansion);
  }

  /**
   * No args constructor.
   */
  public GoldGemCard() {
    super();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    GoldGemCard card = (GoldGemCard) obj;
    return super.prestigePoints == card.prestigePoints && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion;
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost, level, expansion);
  }
}
