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
public class GoldGemCardTest {

  // Testing Object
  static GoldGemCard goldGemCard;

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
    goldGemCard = new GoldGemCard(prestigePoints, cost, cardLevel, expansion);
  }

  @Test
  public void testPrestigePoints() {
    this.goldGemCard =
        new GoldGemCard(prestigePoints, cost, cardLevel, expansion);
    assertEquals(prestigePoints, this.goldGemCard.prestigePoints());
  }

  @Test
  public void testCost() {
    cost = new Gems();
    cost.put(GemColor.GREEN, 1);
    cost.put(GemColor.BLUE, 2);
    cost.put(GemColor.WHITE, 3);
    this.goldGemCard =
        new GoldGemCard(prestigePoints, cost, cardLevel, expansion);

    Gems equCost = new Gems();
    equCost.put(GemColor.GREEN, 1);
    equCost.put(GemColor.BLUE, 2);
    equCost.put(GemColor.WHITE, 3);
    assertEquals(cost, goldGemCard.cost());
  }

  @Test
  public void testLevel() {
    assertEquals(cardLevel, goldGemCard.level());
  }

  @Test
  public void testExpansion() {
    assertEquals(expansion, goldGemCard.expansion());
  }

  @Test
  public void testEqualsByReference() {
    GoldGemCard duplicate = goldGemCard;
    assertTrue(goldGemCard.equals(duplicate));
  }

  @Test
  public void testEqualsNullCard() {
    StandardCard nullCard = new StandardCard();
    nullCard = null;
    assertFalse(goldGemCard.equals(nullCard));
  }

  @Test
  public void testEqualsDifferentClass() {
    Object o = new Object();
    assertFalse(goldGemCard.equals(o));
  }

  @Test
  public void testEqualsByValue() {
    GoldGemCard cardWithEqualValues =
        new GoldGemCard(prestigePoints, cost, cardLevel, expansion);
    assertTrue(goldGemCard.equals(cardWithEqualValues));
  }

  @Test
  public void testNoArgsConstructor() {
    GoldGemCard nullCard = new GoldGemCard();
    assertNull(nullCard.cost);
    assertNull(nullCard.expansion);
    assertNull(nullCard.level);
    assertEquals(nullCard.prestigePoints, 0);
  }

}
