package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.Set;

/**
 * A player's hand.
 *
 * @param gems           the amount of gems the owned
 * @param reservedCards  the reserved cards
 * @param purchasedCards the purchased cards
 * @param visitedNoble   the owned noble
 * @param reservedNoble  the reserved noble
 * @param gemDiscounts   the gem discounts
 */
public record Hand(Gems gems, Set<Card> reservedCards, Set<Card> purchasedCards,
                   Noble visitedNoble, Noble reservedNoble, Gems gemDiscounts) {
}
