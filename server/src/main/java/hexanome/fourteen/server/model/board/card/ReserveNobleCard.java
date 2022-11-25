package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.gem.GemColor;

/**
 * Reserve Noble Card.
 *
 * @param gemDiscount   the amount of gems to be discounted
 * @param discountColor the color of the gems to be discounted
 */
public record ReserveNobleCard(int gemDiscount, GemColor discountColor) implements CardType {
}
