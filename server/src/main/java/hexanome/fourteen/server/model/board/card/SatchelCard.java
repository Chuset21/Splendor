package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Satchel Card.
 */
public final class SatchelCard extends Card {
  private final GemColor gemColor;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param gemColor       the gem color to attach the satchel card to
   */
  public SatchelCard(int prestigePoints, Gems cost, CardLevel level, Expansion expansion,
                     GemColor gemColor) {
    super(prestigePoints, cost, level, expansion);
    this.gemColor = gemColor;
  }

  /**
   * A Getter for the Gem Color of the Satchel Card.
   *
   * @return The Gem Color
   */
  public GemColor gemColor() {
    return gemColor;
  }
}
