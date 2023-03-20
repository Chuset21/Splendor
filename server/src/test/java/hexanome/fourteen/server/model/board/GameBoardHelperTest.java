package hexanome.fourteen.server.model.board;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import hexanome.fourteen.server.control.GsonInstance;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.player.Player;
import hexanome.fourteen.server.model.clientmapper.ServerToClientBoardGameMapper;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(PER_CLASS)
public class GameBoardHelperTest {

  final ServerToClientBoardGameMapper serverToClientBoardGameMapper =
      new ServerToClientBoardGameMapper();

  private static GsonInstance gsonInstance;

  private GameBoard gameBoard;

  @BeforeAll
  public static void setup() {
    gsonInstance = new GsonInstance();
    gsonInstance.initGson();
  }


  @BeforeEach
  public void init() {
    final Set<Expansion> expansions = Set.of(Expansion.STANDARD, Expansion.ORIENT);

    final Player player1 = new Player("player1", new Hand(expansions));
    final Player player2 = new Player("player2", new Hand(expansions));

    gameBoard =
        new GameBoard(expansions, Set.of(player1, player2), "SplendorGameID", player1.uid());
  }

  @Test
  public void testIsCardFaceUpOnBoardAndRemoveCardFromDeck() {
    // This is just so that equals fails on reference check
    final GameBoard g = gsonInstance.gson.fromJson(
        gsonInstance.gson.toJson(gameBoard), GameBoard.class);
    Set<List<Card>> cards = serverToClientBoardGameMapper.map(g).cards();
    do {
      final Set<List<Card>> c = gameBoard.cards();
      cards.stream().flatMap(Collection::stream)
          .map(card -> GameBoardHelper.isCardFaceUpOnBoard(c, card))
          .forEach(Assertions::assertTrue);
      cards.stream().flatMap(Collection::stream)
          .map(card -> GameBoardHelper.removeCardFromDeck(c, card))
          .forEach(Assertions::assertTrue);
      final GameBoard gameBoardCopy = gsonInstance.gson.fromJson(
          gsonInstance.gson.toJson(gameBoard), GameBoard.class);
      cards = serverToClientBoardGameMapper.map(gameBoardCopy).cards();
    } while (cards.stream().mapToLong(List::size).sum() > 0);
  }
}