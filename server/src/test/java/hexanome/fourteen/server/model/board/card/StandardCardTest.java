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
public class StandardCardTest {

  // Testing Object
  static StandardCard standardCard;

  // Test values
  static Gems cost;
  static final int prestigePoints = 3;
  static final CardLevel cardLevel = CardLevel.THREE;
  static final Expansion expansion = Expansion.STANDARD;
  static final int gemDiscount = 1;
  static final GemColor discountColor = GemColor.RED;

  @BeforeAll
  public static void setUp() {
    cost = new Gems();
    cost.put(GemColor.GREEN, 1);
    cost.put(GemColor.BLUE, 2);
    cost.put(GemColor.WHITE, 3);
    standardCard =
        new StandardCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor);
  }

  @Test
  public void testPrestigePoints() {
    this.standardCard =
        new StandardCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor);
    assertEquals(prestigePoints, this.standardCard.prestigePoints());
  }

  @Test
  public void testCost() {
    cost = new Gems();
    cost.put(GemColor.GREEN, 1);
    cost.put(GemColor.BLUE, 2);
    cost.put(GemColor.WHITE, 3);
    this.standardCard =
        new StandardCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor);

    Gems equCost = new Gems();
    equCost.put(GemColor.GREEN, 1);
    equCost.put(GemColor.BLUE, 2);
    equCost.put(GemColor.WHITE, 3);
    assertEquals(cost, standardCard.cost());
  }

  @Test
  public void testLevel() {
    assertEquals(cardLevel, standardCard.level());
  }

  @Test
  public void testExpansion() {
    assertEquals(expansion, standardCard.expansion());
  }

  @Test
  public void testGemDiscount() {
    assertEquals(gemDiscount, standardCard.gemDiscount());
  }

  @Test
  public void testDiscountColor() {
    assertEquals(discountColor, standardCard.discountColor());
  }

  @Test
  public void testEqualsByReference() {
    StandardCard duplicate = standardCard;
    boolean equals = standardCard.equals(duplicate);
    assertEquals(true, equals);
  }

  @Test
  public void testEqualsNullCard() {
    StandardCard nullCard = new StandardCard();
    nullCard = null;
    boolean equals = standardCard.equals(nullCard);
    assertEquals(false, equals);
  }

  @Test
  public void testEqualsDifferentClass() {
    Object o = new Object();
    assertFalse(standardCard.equals(o));
  }
  @Test
  public void testEqualsByValue() {
    StandardCard cardWithEqualValues =
            new StandardCard(prestigePoints, cost, cardLevel, expansion, gemDiscount, discountColor);
    assertTrue(standardCard.equals(cardWithEqualValues));
  }
}