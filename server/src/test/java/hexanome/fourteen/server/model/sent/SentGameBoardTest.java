package hexanome.fourteen.server.model.sent;

import hexanome.fourteen.server.model.board.City;
import hexanome.fourteen.server.model.board.Hand;
import hexanome.fourteen.server.model.board.Noble;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class SentGameBoardTest {
  // Test Object
  static SentGameBoard sentGameBoard;

  // Test Fields
  String playerTurnid = "player1";
  Set<Noble> availableNobles;
  Set<City> availableCities;
  Gems availableGems;
  Set<List<Card>> cards;
  Set<Expansion> expansions;
  Player leadingPlayer;
  Set<Player> players;
  String gameid = "test_id";
  String creator = "test_creator";

  @BeforeEach
  public void setUp() {
    availableCities = new HashSet<>();
    availableNobles = new HashSet<>();
    Gems cost1 = new Gems();
    cost1.put(GemColor.BLUE, 4);
    cost1.put(GemColor.RED, 4);
    availableNobles.add(new Noble(3, cost1));

    Gems cost2 = new Gems();
    cost2.put(GemColor.BLUE, 3);
    cost2.put(GemColor.GREEN, 3);
    cost2.put(GemColor.BLACK, 3);
    availableNobles.add(new Noble(3, cost2));

    Gems cost3 = new Gems();
    cost3.put(GemColor.WHITE, 4);
    cost3.put(GemColor.GREEN, 3);
    availableNobles.add(new Noble(3, cost3));

    availableGems = new Gems();
    availableGems.put(GemColor.GREEN, 5);
    availableGems.put(GemColor.WHITE, 5);
    availableGems.put(GemColor.BLUE, 5);
    availableGems.put(GemColor.BLACK, 5);
    availableGems.put(GemColor.RED, 5);
    availableGems.put(GemColor.GOLD, 4);
    availableCities.add(new City(17, new Gems()));
    expansions = new HashSet<>();
    expansions.add(Expansion.STANDARD);
    expansions.add(Expansion.ORIENT);
    Player player1 = new Player(playerTurnid, new Hand(expansions));
    Player player2 = new Player("player2", new Hand(expansions));
    Player player3 = new Player("player3", new Hand(expansions));
    Player player4 = new Player("player4", new Hand(expansions));
    leadingPlayer = player1;
    players = new HashSet<>();
    players.add(player1);
    players.add(player2);
    players.add(player3);
    players.add(player4);

    sentGameBoard = new SentGameBoard(playerTurnid, availableNobles, availableCities, availableGems,
        cards, expansions, leadingPlayer, players, gameid, creator, false, false);

  }

  @Test
  public void testPlayerTurnID() {
    assertEquals(playerTurnid, sentGameBoard.playerTurnid());
  }
}