package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.card.StandardCard;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class GameBoardTest {

  // Test Object
  GameBoard gameBoard;

  // Test Fields
  int playerTurn = 1;

  Map<Integer, Player> playerTurnMap;
  Set<Noble> availableNobles;

  Gems availableGems;

  Set<List<Card>> cards;
  Set<Expansion> expansions;

  Player leadingPlayer;
  Set<Player> players;
  Player player1;
  Player player2;
  String gameid;
  String creator;

  @BeforeEach
  public void setup() {
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

    availableNobles = new HashSet<>();
    availableNobles.add(new Noble(3, nobleCost));

    expansions = new HashSet<>();
    expansions.add(Expansion.STANDARD);
    expansions.add(Expansion.ORIENT);

    players = new HashSet<>();
    Hand hand = new Hand();
    hand.setPrestigePoints(2);
    player1 = new Player("player1", new Hand());
    player2 = new Player("player2", hand);
    players.add(player1);
    players.add(player2);

    gameid = "SplendorGameID";

    creator = "player1";

    gameBoard =
        new GameBoard(availableNobles, expansions, players, gameid, creator);
  }

  @Test
  public void testPlayerTurn() { assertTrue(gameBoard.playerTurn() <= players.size() - 1);}

  @Test
  public void testPlayerTurnMap() { assertNotNull(gameBoard.playerTurnMap());}

  @Test
  public void testAvailableNobles() { assertEquals(availableNobles, gameBoard.availableNobles());}

  @Test
  public void testAvailableGems() { assertEquals(availableGems, gameBoard.availableGems());}

  @Test
  public void testCards() { assertNotNull(gameBoard.cards());}

  @Test
  public void testExpansions() { assertEquals(expansions, gameBoard.expansions());}

  @Test
  public void testPlayers() { assertEquals(players, gameBoard.players());}

  @Test
  public void testGameid() { assertEquals(gameid, gameBoard.gameid());}

  @Test
  public void testCreator() { assertEquals(creator, gameBoard.creator());}

  @Test
  public void testComputeLeadingPlayer() {

    player2.hand().setPrestigePoints(2);
    gameBoard.computeLeadingPlayer();
    assertEquals(player2.uid(), gameBoard.leadingPlayer().uid());

    for (Player player :gameBoard.players()) {
      player.hand().setPrestigePoints(0);
    }
    gameBoard.computeLeadingPlayer();
    if (gameBoard.leadingPlayer().uid().equals("player1")) {
      assertEquals(player1.uid(), gameBoard.leadingPlayer().uid());
    } else {
      assertEquals(player2.uid(), gameBoard.leadingPlayer().uid());
    }



  }
}