package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.Gems;

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
  public GoldGemCard(int prestigePoints, Gems cost,
                     CardLevel level,
                     Expansion expansion) {
    super(prestigePoints, cost, level, expansion);
  }
}
