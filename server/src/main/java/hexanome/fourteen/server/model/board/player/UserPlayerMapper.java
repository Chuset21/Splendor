package hexanome.fourteen.server.model.board.player;

import hexanome.fourteen.server.Mapper;
import hexanome.fourteen.server.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper from User to Player.
 */
@Component
public class UserPlayerMapper implements Mapper<User, Player> {
  /**
   * Constructor.
   */
  public UserPlayerMapper() {

  }

  @Override
  public Player map(User user) {
    return new Player(user.name());
  }
}
