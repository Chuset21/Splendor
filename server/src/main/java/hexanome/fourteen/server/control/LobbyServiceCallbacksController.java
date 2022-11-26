package hexanome.fourteen.server.control;

import hexanome.fourteen.server.Mapper;
import hexanome.fourteen.server.model.User;
import hexanome.fourteen.server.model.board.GameBoard;
import hexanome.fourteen.server.model.board.Noble;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.player.Player;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle lobby service callbacks.
 */
@RestController
@RequestMapping("/api/games/")
public class LobbyServiceCallbacksController {

  private Map<String, GameBoard> gameManager;
  private final Mapper<User, Player> userPlayerMapper;
  private final Mapper<String, Expansion> stringExpansionMapper;

  public LobbyServiceCallbacksController(@Autowired Mapper<User, Player> userPlayerMapper,
                                         @Autowired
                                         Mapper<String, Expansion> stringExpansionMapper) {
    this.userPlayerMapper = userPlayerMapper;
    this.stringExpansionMapper = stringExpansionMapper;
  }

  @PostConstruct
  private void initGameManager() {
    gameManager = new HashMap<>();
  }

  /**
   * Create a game given the specified parameters.
   *
   * @param gameid         The game id for the created game
   * @param launchGameForm The information needed to create the game
   */
  @PutMapping(value = "{gameid}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchGame(@PathVariable String gameid,
                                           @RequestBody LaunchGameForm launchGameForm) {
    gameManager.put(gameid, // TODO add checks
        createGame(gameid, launchGameForm));
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  private GameBoard createGame(String gameid, LaunchGameForm launchGameForm) {
    Set<Noble> nobles = null; // TODO
    List<List<Card>> cards = null; // TODO
    Set<Expansion> expansions = Set.of(stringExpansionMapper.map(launchGameForm.gameType()));
    Set<Player> players = Arrays.stream(launchGameForm.players()).map(userPlayerMapper::map)
        .collect(Collectors.toSet());
    String creator = launchGameForm.creator();

    return new GameBoard(nobles, cards, expansions, players, gameid, creator);
  }

  @DeleteMapping("{gameid}")
  public ResponseEntity<String> deleteGame(@PathVariable String gameid) {
    gameManager.remove(gameid); // TODO implement properly
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}