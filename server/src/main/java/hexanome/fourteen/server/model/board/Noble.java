package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.gem.Gems;

/**
 * Noble
 * @param prestigePoints The amount of prestige points associated with the noble
 * @param cost The amount of gem discounts needed to acquire the noble
 */
public record Noble(int prestigePoints, Gems cost) {
}
