package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Game Board
 * For now this is a record, but it will be converted into a class with the proper functionality.
 * We might have to include a different class, GameBoard, with no functionality,
 * and only the necessary information to send to the clients (We'll see).
 * If so this might be true for more than just this class.
 * @param playerTurn The current player's turn
 * @param playerTurnMap A map to convert player turn to the actual player
 * @param availableNobles The nobles on the board
 * @param availableGems The gems in the bank
 * @param cards The cards on the board, including the one's that are not face up.
 *              I'm interpreting it as each list being a list of a certain level of cards,
 *              with the first x amount in each list being the cards that are face up.
 *              We can use the sublist method for this I believe.
 * @param expansions The set of expansions
 * @param leadingPlayer The current leading player
 * @param players The players
 * @param gameID The game ID
 */
public record GameBoard(int playerTurn, Map<Integer, String> playerTurnMap,
                        Set<Noble> availableNobles, Gems availableGems, List<List<Card>> cards,
                        Set<Expansion>expansions, Player leadingPlayer, Set<Player> players,
                        String gameID) {
}
