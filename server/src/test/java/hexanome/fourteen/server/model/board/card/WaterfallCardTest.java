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
public class WaterfallCardTest {

  // Testing Object
  static WaterfallCard waterfallCard;

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
    cost.put(GemColor.GREEN, 1);
    cost.put(GemColor.BLUE, 2);
    cost.put(GemColor.WHITE, 3);
    waterfallCard =
        new WaterfallCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor);
  }

  @Test
  public void testPrestigePoints() {
    this.waterfallCard =
        new WaterfallCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor);
    assertEquals(prestigePoints, this.waterfallCard.prestigePoints());
  }

  @Test
  public void testCost() {
    cost = new Gems();
    cost.put(GemColor.GREEN, 1);
    cost.put(GemColor.BLUE, 2);
    cost.put(GemColor.WHITE, 3);
    this.waterfallCard =
        new WaterfallCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor);

    Gems equCost = new Gems();
    equCost.put(GemColor.GREEN, 1);
    equCost.put(GemColor.BLUE, 2);
    equCost.put(GemColor.WHITE, 3);
    assertEquals(cost, waterfallCard.cost());
  }

  @Test
  public void testLevel() {
    assertEquals(cardLevel, waterfallCard.level());
  }

  @Test
  public void testExpansion() {
    assertEquals(expansion, waterfallCard.expansion());
  }

  @Test
  public void testGemDiscount() { assertEquals(gemDiscount, waterfallCard.gemDiscount()); }

  @Test
  public void testDiscountColor() { assertEquals(discountColor, waterfallCard.discountColor()); }

  @Test
  public void testEqualsByReference() {
    WaterfallCard duplicate = waterfallCard;
    assertTrue(waterfallCard.equals(duplicate));
  }

  @Test
  public void testEqualsNullCard() {
    WaterfallCard nullCard = new WaterfallCard();
    nullCard = null;
    assertFalse(waterfallCard.equals(nullCard));
  }

  @Test
  public void testEqualsDifferentClass() {
    Object o = new Object();
    assertFalse(waterfallCard.equals(o));
  }

  @Test
  public void testEqualsByValue() {
    WaterfallCard cardWithEqualValues =
        new WaterfallCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor);
    assertTrue(waterfallCard.equals(cardWithEqualValues));
  }

  @Test
  public void testNoArgsConstructor() {
    WaterfallCard nullCard = new WaterfallCard();
    assertNull(nullCard.cost);
    assertNull(nullCard.expansion);
    assertNull(nullCard.level);
    assertEquals(nullCard.prestigePoints, 0);
    assertEquals(nullCard.gemDiscount(), 0);
    assertNull(nullCard.discountColor());
    assertNull(nullCard.cardToTake());
  }
}
