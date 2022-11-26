package hexanome.fourteen.server.control;

import hexanome.fourteen.server.Mapper;
import hexanome.fourteen.server.model.board.GameBoard;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Controller to handle lobby service callbacks.
 */
@RestController
@RequestMapping("/api/games/")
public class LobbyServiceCallbacksController {

  private Map<String, GameBoard> gameManager;
  private final Mapper<CreateGameForm, GameBoard> createGameFormGameBoardMapper;

  public LobbyServiceCallbacksController(
      @Autowired Mapper<CreateGameForm, GameBoard> createGameFormGameBoardMapper) {
    this.createGameFormGameBoardMapper = createGameFormGameBoardMapper;
  }

  @PostConstruct
  private void initGameManager() {
    gameManager = new HashMap<>();
  }

  /**
   * Create a game given the specified parameters.
   *
   * @param gameid      The game id for the created game
   * @param putGameForm The information needed to create the game
   */
  @PutMapping(value = "{gameid}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchGame(@PathVariable String gameid,
                                           @RequestBody PutGameForm putGameForm) {
    gameManager.put(gameid, // TODO add checks
        createGameFormGameBoardMapper.map(new CreateGameForm(putGameForm, gameid)));
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @DeleteMapping("{gameid}")
  public ResponseEntity<String> deleteGame(@PathVariable String gameid) {
    gameManager.remove(gameid); // TODO implement properly
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}