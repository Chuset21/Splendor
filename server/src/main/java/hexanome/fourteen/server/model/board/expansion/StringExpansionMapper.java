package hexanome.fourteen.server.model.board.expansion;

import hexanome.fourteen.server.Mapper;
import org.springframework.stereotype.Component;

/**
 * Singleton string to expansion mapper.
 */
@Component
public class StringExpansionMapper implements Mapper<String, Expansion> {
  @Override
  public Expansion map(String s) {
    return Expansion.valueOf(s);
  }
}
