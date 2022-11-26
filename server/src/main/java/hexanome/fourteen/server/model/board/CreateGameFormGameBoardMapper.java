package hexanome.fourteen.server.model.board;

import hexanome.fourteen.server.Mapper;
import hexanome.fourteen.server.control.CreateGameForm;
import hexanome.fourteen.server.control.LaunchGameForm;
import hexanome.fourteen.server.model.User;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.player.Player;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper from CreateGameForm to GameBoard.
 */
@Component
public class CreateGameFormGameBoardMapper implements Mapper<CreateGameForm, GameBoard> {

  private final Mapper<User, Player> userPlayerMapper;
  private final Mapper<String, Expansion> stringExpansionMapper;

  public CreateGameFormGameBoardMapper(@Autowired Mapper<User, Player> userPlayerMapper,
                                       @Autowired Mapper<String, Expansion> stringExpansionMapper) {
    this.userPlayerMapper = userPlayerMapper;
    this.stringExpansionMapper = stringExpansionMapper;
  }

  @Override
  public GameBoard map(CreateGameForm createGameForm) {
    final LaunchGameForm launchGameForm = createGameForm.launchGameForm();
    Set<Noble> nobles = null; // TODO
    List<List<Card>> cards = null; // TODO
    Set<Expansion> expansions = Set.of(stringExpansionMapper.map(launchGameForm.gameType()));
    Set<Player> players = Arrays.stream(launchGameForm.players()).map(userPlayerMapper::map)
        .collect(Collectors.toSet());
    String gameid = createGameForm.gameid();
    String creator = launchGameForm.creator();

    return new GameBoard(nobles, cards, expansions, players, gameid, creator);
  }
}
