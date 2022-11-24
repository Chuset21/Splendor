package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.gem.GemColor;

/**
 * Satchel Card
 * @param gemColor the gem color to attach the satchel card to
 */
public record SatchelCard(GemColor gemColor) implements CardType {
}
