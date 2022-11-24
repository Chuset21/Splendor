package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.gem.GemColor;

/**
 * Waterfall Card
 * @param gemDiscount   the amount of gems to be discounted
 * @param discountColor the color of the gems to be discounted
 * @param freeCardLevel the level of the card that you get for free
 */
public record WaterfallCard(int gemDiscount, GemColor discountColor, CardLevel freeCardLevel)
    implements CardType {
}
