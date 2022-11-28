package hexanome.fourteen.server.model.board.expansion;

import hexanome.fourteen.server.Mapper;
import org.springframework.stereotype.Component;

/**
 * Singleton expansion to string mapper.
 */
@Component
public class ExpansionStringMapper implements Mapper<Expansion, String> {

  /**
   * Constructor.
   */
  public ExpansionStringMapper() {
  }

  /**
   * A Map of Expansion which returns the name of the Expansion.
   *
   * @param expansion The Expansion
   * @return The Name of the Expansion
   */
  @Override
  public String map(Expansion expansion) {
    return expansion.name();
  }
}
