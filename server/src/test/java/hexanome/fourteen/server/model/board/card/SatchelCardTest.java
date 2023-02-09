package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class SatchelCardTest {

  // Testing Object
  static SatchelCard satchelCard;

  // Test values
  static Gems cost;
  static final int prestigePoints = 3;
  static final CardLevel cardLevel = CardLevel.THREE;
  static final Expansion expansion = Expansion.STANDARD;

  @BeforeAll
  public static void setUp() {
    cost = new Gems();
    cost.put(GemColor.GREEN, 1);
    cost.put(GemColor.BLUE, 2);
    cost.put(GemColor.WHITE, 3);
    satchelCard = new SatchelCard(prestigePoints, cost, cardLevel, expansion);
  }

  @Test
  public void testPrestigePoints() {
    this.satchelCard =
        new SatchelCard(prestigePoints, cost, cardLevel, expansion);
    assertEquals(prestigePoints, this.satchelCard.prestigePoints());
  }

  @Test
  public void testCost() {
    cost = new Gems();
    cost.put(GemColor.GREEN, 1);
    cost.put(GemColor.BLUE, 2);
    cost.put(GemColor.WHITE, 3);
    this.satchelCard =
        new SatchelCard(prestigePoints, cost, cardLevel, expansion);

    Gems equCost = new Gems();
    equCost.put(GemColor.GREEN, 1);
    equCost.put(GemColor.BLUE, 2);
    equCost.put(GemColor.WHITE, 3);
    assertEquals(cost, satchelCard.cost());
  }

  @Test
  public void testLevel() {
    assertEquals(cardLevel, satchelCard.level());
  }

  @Test
  public void testExpansion() {
    assertEquals(expansion, satchelCard.expansion());
  }

  @Test
  public void testEqualsByReference() {
    SatchelCard duplicate = satchelCard;
    assertTrue(satchelCard.equals(duplicate));
  }

  @Test
  public void testEqualsNullCard() {
    SatchelCard nullCard = new SatchelCard();
    nullCard = null;
    assertFalse(satchelCard.equals(nullCard));
  }

  @Test
  public void testEqualsDifferentClass() {
    Object o = new Object();
    assertFalse(satchelCard.equals(o));
  }

  @Test
  public void testEqualsByValue() {
    SatchelCard cardWithEqualValues =
        new SatchelCard(prestigePoints, cost, cardLevel, expansion);
    assertTrue(satchelCard.equals(cardWithEqualValues));
  }

  @Test
  public void testNoArgsConstructor() {
    SatchelCard nullCard = new SatchelCard();
    assertNull(nullCard.cost);
    assertNull(nullCard.expansion);
    assertNull(nullCard.level);
    assertEquals(nullCard.prestigePoints, 0);
    assertNull(nullCard.cardToAttach());
  }
}