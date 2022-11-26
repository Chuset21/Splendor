package hexanome.fourteen.server.control;

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

  @PutMapping(value = "{gameid}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchGame(@PathVariable String gameid,
                                           @RequestBody PutGameForm putGameForm) {
    new CreateGameForm(putGameForm, gameid); // TODO use
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @DeleteMapping("{gameid}")
  public ResponseEntity<String> deleteGame(@PathVariable String gameid) {
    return ResponseEntity.ok("Game ID: %s received".formatted(gameid)); // TODO implement
  }

}
