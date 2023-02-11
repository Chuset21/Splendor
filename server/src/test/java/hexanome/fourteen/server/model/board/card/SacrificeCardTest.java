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
public class SacrificeCardTest {

  // Testing Object
  static SacrificeCard sacrificeCard;

  // Test values
  static Gems cost;
  static final int prestigePoints = 3;
  static final CardLevel cardLevel = CardLevel.THREE;
  static final Expansion expansion = Expansion.STANDARD;
  static final int gemDiscount = 1;
  static final GemColor discountColor = GemColor.RED;
  static final GemColor sacrificeColor = GemColor.BLUE;

  @BeforeAll
  public static void setUp() {
    cost = new Gems();
    sacrificeCard =
        new SacrificeCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor, sacrificeColor);
  }

  @Test
  public void testPrestigePoints() {
    this.sacrificeCard =
        new SacrificeCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor, sacrificeColor);
    assertEquals(prestigePoints, this.sacrificeCard.prestigePoints());
  }

  @Test
  public void testCost() {
    cost = new Gems();
    cost.put(GemColor.GREEN, 1);
    cost.put(GemColor.BLUE, 2);
    cost.put(GemColor.WHITE, 3);
    this.sacrificeCard =
        new SacrificeCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor, sacrificeColor);

    Gems equCost = new Gems();
    equCost.put(GemColor.GREEN, 1);
    equCost.put(GemColor.BLUE, 2);
    equCost.put(GemColor.WHITE, 3);
    assertEquals(cost, sacrificeCard.cost());
  }

  @Test
  public void testLevel() {
    assertEquals(cardLevel, sacrificeCard.level());
  }

  @Test
  public void testExpansion() {
    assertEquals(expansion, sacrificeCard.expansion());
  }

  @Test
  public void testGemDiscount() { assertEquals(gemDiscount, sacrificeCard.gemDiscount()); }

  @Test
  public void testDiscountColor() { assertEquals(discountColor, sacrificeCard.discountColor()); }

  @Test
  public void testSacrificeColor() { assertEquals(sacrificeColor, sacrificeCard.sacrificeColor()); }

  @Test
  public void testEqualsByReference() {
    SacrificeCard duplicate = sacrificeCard;
    assertTrue(sacrificeCard.equals(duplicate));
  }

  @Test
  public void testEqualsNullCard() {
    SacrificeCard nullCard = new SacrificeCard();
    nullCard = null;
    assertFalse(sacrificeCard.equals(nullCard));
  }

  @Test
  public void testEqualsDifferentClass() {
    Object o = new Object();
    assertFalse(sacrificeCard.equals(o));
  }

  @Test
  public void testEqualsByValue() {
    SacrificeCard cardWithEqualValues =
        new SacrificeCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor, sacrificeColor );
    assertTrue(sacrificeCard.equals(cardWithEqualValues));
  }

  @Test
  public void testNoArgsConstructor() {
    SacrificeCard nullCard = new SacrificeCard();
    assertNull(nullCard.cost);
    assertNull(nullCard.expansion);
    assertNull(nullCard.level);
    assertEquals(nullCard.prestigePoints, 0);
    assertEquals(nullCard.gemDiscount(), 0);
    assertNull(nullCard.discountColor());
    assertNull(nullCard.sacrificeColor());
  }
}
