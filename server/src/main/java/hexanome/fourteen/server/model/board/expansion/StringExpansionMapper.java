package hexanome.fourteen.server.model.board.expansion;

import hexanome.fourteen.server.Mapper;
import org.springframework.stereotype.Component;

/**
 * Singleton string to expansion mapper.
 */
@Component
public class StringExpansionMapper implements Mapper<String, Expansion> {

  /**
   * Constructor.
   */
  public StringExpansionMapper() {

  }

  /**
   * A Map of String to Expansion.
   *
   * @param s The Object to Map
   * @return The Value of Object passed
   */
  @Override
  public Expansion map(String s) {
    return Expansion.valueOf(s);
  }
}
