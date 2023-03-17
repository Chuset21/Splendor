package hexanome.fourteen.server.model.board.player;

import hexanome.fourteen.server.model.board.Hand;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class PlayerTest {

  static Player player;

  @BeforeAll
  public static void setUp() {
    player = new Player("user", Set.of(Expansion.STANDARD));
  }

  @Test
  public void testPlayerConstructor() {
    final Player p = new Player("user", new Hand(Set.of(Expansion.STANDARD)));
    assertEquals(player.uid(), p.uid());
    assertEquals(player.hand(), p.hand());
  }

  @Test
  public void testUid() {
    assertEquals("user", player.uid());
  }

  @Test
  public void testHand() {
    assertEquals(new Hand(Set.of(Expansion.STANDARD)), player.hand());
  }

  @Test
  public void testNoArgs() {
    assertNull(new Player().uid());
  }
}