package hexanome.fourteen.server.control;

import org.springframework.beans.factory.annotation.Autowired;
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

  private final GsonInstance gsonInstance;

  /**
   * Constructor.
   *
   * @param gsonInstance common gson instance
   */
  public LobbyServiceCallbacksController(@Autowired GsonInstance gsonInstance) {
    this.gsonInstance = gsonInstance;
  }

  @PutMapping(value = "{gameid}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchGame(@PathVariable String gameid,
                                           @RequestBody LaunchGameForm launchGameForm) {
    return null;
  }

  @DeleteMapping("{gameid}")
  public ResponseEntity<String> deleteGame(@PathVariable String gameid) {
    return null;
  }

}
