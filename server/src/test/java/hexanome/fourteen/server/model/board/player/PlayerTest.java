package hexanome.fourteen.server.model.board.player;

import hexanome.fourteen.server.model.board.Hand;
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
    player = new Player("user");
  }

  @Test
  public void testPlayerConstructor() {
    final Player p = new Player("user", new Hand());
    assertEquals(player.uid(), p.uid());
    assertEquals(player.hand(), p.hand());
  }

  @Test
  public void testUid() {
    assertEquals("user", player.uid());
  }

  @Test
  public void testHand() {
    assertEquals(new Hand(), player.hand());
  }
}