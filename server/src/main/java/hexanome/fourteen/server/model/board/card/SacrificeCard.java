package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.gem.GemColor;

/**
 * Sacrifice Card.
 *
 * @param gemDiscount    the amount of gems to be discounted
 * @param discountColor  the color of the gems to be discounted
 * @param sacrificeColor the gem color of the cards to be sacrificed
 */
public record SacrificeCard(int gemDiscount, GemColor discountColor, GemColor sacrificeColor)
    implements CardType {
}
