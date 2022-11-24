package hexanome.fourteen.server.model.board;

import java.util.Set;

// TODO change set type

/**
 * Noble
 * @param prestigePoints The amount of prestige points needed to purchase the noble
 * @param cost ???
 */
public record Noble(int prestigePoints, Set<Integer> cost) {
}
