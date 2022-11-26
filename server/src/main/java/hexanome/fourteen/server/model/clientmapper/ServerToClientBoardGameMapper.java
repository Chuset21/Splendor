package hexanome.fourteen.server.model.clientmapper;

import hexanome.fourteen.server.Mapper;
import hexanome.fourteen.server.model.board.GameBoard;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.player.Player;
import hexanome.fourteen.server.model.sent.SentGameBoard;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mapper used to only send the necessary information to the client.
 */
public class ServerToClientBoardGameMapper implements Mapper<GameBoard, SentGameBoard> {
  @Override
  public SentGameBoard map(GameBoard gameBoard) {
    final Map<Integer, Player> playerTurnMap = gameBoard.playerTurnMap();
    final List<List<Card>> sentCards = new ArrayList<>();
    for (List<Card> cards : gameBoard.cards()) {
      if (!cards.isEmpty()) {
        final int limit =
            Integer.min(cards.size(), cards.get(0).expansion() == Expansion.STANDARD ? 4 : 2);
        sentCards.add(cards.subList(0, limit));
      }
    }

    return new SentGameBoard(playerTurnMap.get(gameBoard.playerTurn()).uid(),
        gameBoard.availableNobles(), gameBoard.availableGems(), sentCards,
        gameBoard.leadingPlayer(), gameBoard.players(), gameBoard.gameid(), gameBoard.creator());
  }
}
