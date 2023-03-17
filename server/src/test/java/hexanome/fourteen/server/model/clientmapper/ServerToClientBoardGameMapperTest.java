package hexanome.fourteen.server.model.clientmapper;

import hexanome.fourteen.server.model.board.GameBoard;
import hexanome.fourteen.server.model.board.Hand;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import hexanome.fourteen.server.model.sent.SentGameBoard;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class ServerToClientBoardGameMapperTest {

  // Test Object
  ServerToClientBoardGameMapper serverToClientBoardGameMapper = new ServerToClientBoardGameMapper();

  // Test Fields
  GameBoard gameBoard;
  Gems availableGems;
  Set<Expansion> expansions;
  Set<Player> players;
  Player player1;
  Player player2;
  String gameid;
  String creator;

  @BeforeAll
  void setup() {
    //gameBoard = new GameBoard();
    Gems nobleCost = new Gems();
    nobleCost.put(GemColor.GREEN, 1);
    nobleCost.put(GemColor.BLUE, 2);
    nobleCost.put(GemColor.WHITE, 3);

    availableGems = new Gems();
    availableGems.put(GemColor.WHITE, 4);
    availableGems.put(GemColor.GREEN, 4);
    availableGems.put(GemColor.BLUE, 4);
    availableGems.put(GemColor.GOLD, 5);
    availableGems.put(GemColor.BLACK, 4);
    availableGems.put(GemColor.RED, 4);

    expansions = new HashSet<>();
    expansions.add(Expansion.ORIENT);

    players = new HashSet<>();
    Hand hand = new Hand(expansions);
    hand.setPrestigePoints(2);
    player1 = new Player("player1", new Hand(expansions));
    player2 = new Player("player2", hand);
    players.add(player1);
    players.add(player2);

    gameid = "SplendorGameID";

    creator = "player1";

    gameBoard =
        new GameBoard(expansions, players, gameid, creator);
  }

  @Test
  public void testSentGameBoard() {
    SentGameBoard sentGameBoard = serverToClientBoardGameMapper.map(gameBoard);
    assertEquals(sentGameBoard.playerTurnid(),
        gameBoard.playerTurnMap().get(gameBoard.playerTurn()).uid());
    assertEquals(sentGameBoard.availableNobles(), gameBoard.availableNobles());
    assertEquals(sentGameBoard.availableCities(), gameBoard.availableCities());
    assertEquals(sentGameBoard.availableGems(), gameBoard.availableGems());
    assertEquals(sentGameBoard.expansions(), gameBoard.expansions());
    assertEquals(sentGameBoard.leadingPlayer(), gameBoard.leadingPlayer());
    assertEquals(sentGameBoard.players(), gameBoard.players());
    assertEquals(sentGameBoard.gameid(), gameBoard.gameid());
    assertEquals(sentGameBoard.creator(), gameBoard.creator());
  }

}
