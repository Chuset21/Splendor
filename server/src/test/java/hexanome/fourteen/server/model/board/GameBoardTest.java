package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
    String gameid;
    String creator;

    @BeforeEach
    void setup() {
        //gameBoard = new GameBoard();
    }


}