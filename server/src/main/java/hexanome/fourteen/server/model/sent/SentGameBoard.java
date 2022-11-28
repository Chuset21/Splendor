package hexanome.fourteen.server.model.sent;

import hexanome.fourteen.server.model.board.Noble;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import java.util.List;
import java.util.Set;

/**
 * Game Board to be sent to clients.
 *
 * @param playerTurnid    The player's user ID whose turn it is.
 * @param availableNobles The available nobles.
 * @param availableGems   The gems in the bank.
 * @param cards           The cards that are face up.
 * @param expansions      The expansions for the game.
 * @param leadingPlayer   The current leading player.
 * @param players         The players.
 * @param gameid          The game ID.
 * @param creator         The creator.
 */
public record SentGameBoard(String playerTurnid, Set<Noble> availableNobles, Gems availableGems,
                            Set<List<Card>> cards, Set<Expansion> expansions, Player leadingPlayer,
                            Set<Player> players, String gameid, String creator) {
}