package hexanome.fourteen.server.control;

import hexanome.fourteen.server.Mapper;
import hexanome.fourteen.server.model.User;
import hexanome.fourteen.server.model.board.GameBoard;
import hexanome.fourteen.server.model.board.Noble;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.player.Player;
import hexanome.fourteen.server.model.sent.SentGameBoard;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle lobby service callbacks.
 */
@RestController
@RequestMapping("/api/games/")
public class GameHandlerController {

  private final Map<Set<String>, GameBoard> gameManager;
  private final Mapper<User, Player> userPlayerMapper;
  private final Mapper<String, Expansion> stringExpansionMapper;
  private final Mapper<GameBoard, SentGameBoard> gameBoardMapper;
  private final GsonInstance gsonInstance;

  /**
   * Constructor.
   */
  public GameHandlerController(@Autowired Mapper<User, Player> userPlayerMapper,
                               @Autowired Mapper<String, Expansion> stringExpansionMapper,
                               @Autowired Mapper<GameBoard, SentGameBoard> gameBoardMapper,
                               @Autowired GsonInstance gsonInstance) {
    this.userPlayerMapper = userPlayerMapper;
    this.stringExpansionMapper = stringExpansionMapper;
    this.gameBoardMapper = gameBoardMapper;
    this.gsonInstance = gsonInstance;
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
    gameManager.put(
        Arrays.stream(launchGameForm.players()).map(User::name).collect(Collectors.toSet()),
        createGame(gameid, launchGameForm)); // TODO add checks
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  private GameBoard createGame(String gameid, LaunchGameForm launchGameForm) {
    Set<Noble> nobles = null; // TODO

    Set<Expansion> expansions = Set.of(stringExpansionMapper.map(launchGameForm.gameType()));
    Set<Player> players = Arrays.stream(launchGameForm.players()).map(userPlayerMapper::map)
        .collect(Collectors.toSet());
    String creator = launchGameForm.creator();

    return new GameBoard(nobles, expansions, players, gameid, creator);
  }

  @DeleteMapping("{gameid}")
  public ResponseEntity<String> deleteGame(@PathVariable String gameid) {
    removeGame(gameid); // TODO implement properly
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  /**
   * Remove a game with the matching game ID.
   *
   * @param gameid game ID to remove
   * @return true if the game was successfully removed, false otherwise.
   */
  private boolean removeGame(String gameid) {
    for (Map.Entry<Set<String>, GameBoard> entry : gameManager.entrySet()) {
      if (entry.getValue().gameid().equals(gameid)) {
        gameManager.remove(entry.getKey());
        return true;
      }
    }
    return false;
  }

  private GameBoard getGame(String username) {
    for (Map.Entry<Set<String>, GameBoard> entry : gameManager.entrySet()) {
      if (entry.getKey().contains(username)) {
        return entry.getValue();
      }
    }
    return null;
  }

  /**
   * Get a game board.
   *
   * @param accessToken The user's access token who is sending the request
   */
  @GetMapping(value = "board", produces = "application/json; charset=utf-8")
  public ResponseEntity<String> retrieveGame(@RequestParam("access_token") String accessToken) {
    final String username = getUsername(accessToken);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid access token");
    }

    final GameBoard gameBoard = getGame(username);
    if (gameBoard == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not found");
    } else {
      final SentGameBoard sentGameBoard = gameBoardMapper.map(gameBoard);
      return ResponseEntity.status(HttpStatus.OK)
          .body(gsonInstance.gson.toJson(sentGameBoard, SentGameBoard.class));
    }
  }

  private String getUsername(String accessToken) {
    return Unirest.post("%soauth/username".formatted(ServerService.LS_LOCATION))
        .queryString("access_token", accessToken).asString().getBody();
  }


}