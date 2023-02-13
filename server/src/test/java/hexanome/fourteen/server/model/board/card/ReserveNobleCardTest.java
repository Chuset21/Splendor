package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class ReserveNobleCardTest {

  // Testing Object
  static ReserveNobleCard reserveNobleCard;

  // Test values
  static Gems cost;
  static final int prestigePoints = 3;
  static final CardLevel cardLevel = CardLevel.THREE;
  static final Expansion expansion = Expansion.STANDARD;
  static final GemColor discountColor = GemColor.BLUE;

  @BeforeEach
  public void setUp() {
    cost = new Gems();
    cost.put(GemColor.GREEN, 1);
    cost.put(GemColor.BLUE, 2);
    cost.put(GemColor.WHITE, 3);
    reserveNobleCard =
        new ReserveNobleCard(prestigePoints, cost, cardLevel, expansion, discountColor);
  }

  @Test
  public void testPrestigePoints() {
    assertEquals(prestigePoints, reserveNobleCard.prestigePoints());
  }

  @Test
  public void testCost() {
    Gems equCost = new Gems(cost);
    assertEquals(equCost, reserveNobleCard.cost());
  }

  @Test
  public void testLevel() {
    assertEquals(cardLevel, reserveNobleCard.level());
  }

  @Test
  public void testExpansion() {
    assertEquals(expansion, reserveNobleCard.expansion());
  }

  @Test
  public void testDiscountColor() {
    assertEquals(discountColor, reserveNobleCard.discountColor());
  }

  @Test
  public void testEqualsByReference() {
    ReserveNobleCard duplicate = reserveNobleCard;
    assertTrue(reserveNobleCard.equals(duplicate));
  }

  @Test
  public void testEqualsNullCard() {
    ReserveNobleCard nullCard = new ReserveNobleCard();
    nullCard = null;
    assertFalse(reserveNobleCard.equals(nullCard));
  }

  @Test
  public void testEqualsDifferentClass() {
    Object o = new Object();
    assertFalse(reserveNobleCard.equals(o));
  }

  @Test
  public void testEqualsByValue() {
    ReserveNobleCard cardWithEqualValues =
        new ReserveNobleCard(prestigePoints, cost, cardLevel, expansion, discountColor);
    assertTrue(reserveNobleCard.equals(cardWithEqualValues));
  }

  @Test
  public void testNoArgsConstructor() {
    ReserveNobleCard nullCard = new ReserveNobleCard();
    assertNull(nullCard.cost);
    assertNull(nullCard.expansion);
    assertNull(nullCard.level);
    assertEquals(nullCard.prestigePoints, 0);
    assertNull(nullCard.discountColor());
    assertNull(nullCard.nobleToReserve());
  }
}