package hexanome.fourteen.server.model.board.expansion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
class ExpansionStringMapperTest {

  static ExpansionStringMapper expansionMapper;

  @BeforeAll
  public static void setUp() {
    expansionMapper = new ExpansionStringMapper();
  }

  @Test
  public void testNoArgsConstructor() {
    ExpansionStringMapper mapper = new ExpansionStringMapper();

    Assertions.assertEquals(Expansion.STANDARD.name(), mapper.map(Expansion.STANDARD));
  }

  @Test
  public void testMap() {
    Assertions.assertEquals(Expansion.ORIENT.name(), expansionMapper.map(Expansion.ORIENT));
    Assertions.assertEquals(Expansion.STRONGHOLDS.name(),
        expansionMapper.map(Expansion.STRONGHOLDS));
    Assertions.assertEquals(Expansion.TRADING_POSTS.name(),
        expansionMapper.map(Expansion.TRADING_POSTS));
    Assertions.assertEquals(Expansion.CITIES.name(), expansionMapper.map(Expansion.CITIES));
  }
}