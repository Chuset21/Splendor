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
  static final GemColor discountColor = GemColor.RED;

  @BeforeAll
  public static void setUp() {
    cost = new Gems();
    cost.put(GemColor.GREEN, 1);
    cost.put(GemColor.BLUE, 2);
    cost.put(GemColor.WHITE, 3);
    standardCard =
        new StandardCard(prestigePoints, cost, cardLevel, expansion, discountColor);
  }

  @Test
  public void testPrestigePoints() {
    standardCard =
        new StandardCard(prestigePoints, cost, cardLevel, expansion, discountColor);
    assertEquals(prestigePoints, standardCard.prestigePoints());
  }

  @Test
  public void testCost() {
    assertEquals(new Gems(cost), standardCard.cost());
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
  public void testDiscountColor() {
    assertEquals(discountColor, standardCard.discountColor());
  }

  @Test
  public void testEqualsByReference() {
    StandardCard duplicate = standardCard;
    boolean equals = standardCard.equals(duplicate);
    assertTrue(equals);
  }

  @Test
  public void testEqualsNullCard() {
    StandardCard nullCard = new StandardCard();
    nullCard = null;
    boolean equals = standardCard.equals(nullCard);
    assertFalse(equals);
  }

  @Test
  public void testEqualsDifferentClass() {
    Object o = new Object();
    assertFalse(standardCard.equals(o));
  }
  @Test
  public void testEqualsByValue() {
    StandardCard cardWithEqualValues =
            new StandardCard(prestigePoints, cost, cardLevel, expansion, discountColor);
    assertTrue(standardCard.equals(cardWithEqualValues));
  }
}