package hexanome.fourteen.server.model.board.expansion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
class StringExpansionMapperTest {

  static StringExpansionMapper stringMapper;

  @BeforeAll
  public static void setUp() {
    stringMapper = new StringExpansionMapper();
  }

  @Test
  public void testNoArgsConstructor() {
    StringExpansionMapper mapper = new StringExpansionMapper();

    Assertions.assertEquals(Expansion.STANDARD, mapper.map(Expansion.STANDARD.name()));
  }

  @Test
  public void map() {
    Assertions.assertEquals(Expansion.ORIENT, stringMapper.map(Expansion.ORIENT.name()));
    Assertions.assertEquals(Expansion.STRONGHOLDS,
        stringMapper.map(Expansion.STRONGHOLDS.name()));
    Assertions.assertEquals(Expansion.TRADING_POSTS,
        stringMapper.map(Expansion.TRADING_POSTS.name()));
    Assertions.assertEquals(Expansion.CITIES, stringMapper.map(Expansion.CITIES.name()));
  }
}