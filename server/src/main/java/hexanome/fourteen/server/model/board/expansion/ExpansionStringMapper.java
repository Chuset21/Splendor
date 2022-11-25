package hexanome.fourteen.server.model.board.expansion;

import hexanome.fourteen.server.Mapper;
import org.springframework.stereotype.Component;

/**
 * Singleton expansion to string mapper.
 */
@Component
public class ExpansionStringMapper implements Mapper<Expansion, String> {
  @Override
  public String map(Expansion expansion) {
    return expansion.name();
  }
}
