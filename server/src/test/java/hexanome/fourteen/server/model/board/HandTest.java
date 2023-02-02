package hexanome.fourteen.server.model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HandTest {
  Hand hand;

  @BeforeEach
  public void init() {
    hand = new Hand();
  }

  @Test
  public void testConstructor() {
    Hand handToTest = new Hand(0, null, null, null, null, null, null);
    assertEquals(0, handToTest.prestigePoints());
    assertNull(handToTest.gems());
    assertNull(handToTest.reservedCards());
    assertNull(handToTest.purchasedCards());
    assertNull(handToTest.visitedNobles());
    assertNull(handToTest.reservedNoble());
    assertNull(handToTest.gemDiscounts());
  }

  @Test
  public void testSetPrestigePoints() {
    hand.setPrestigePoints(0);
    assertEquals(0, hand.prestigePoints());
  }

  @Test
  public void testSetGems() {
    hand.setGems(null);
    assertNull(hand.gems());
  }

  @Test
  public void testSetReservedNoble() {
    hand.setReservedNoble(null);
    assertNull(hand.reservedNoble());
  }

  @Test
  public void testSetGemDiscounts() {
    hand.setGemDiscounts(null);
    assertNull(hand.gemDiscounts());
  }

  @Test
  public void testIncrementPrestigePoints() {
    hand.incrementPrestigePoints(2);
    assertEquals(2, hand.prestigePoints());
    hand.incrementPrestigePoints(2);
    assertEquals(4, hand.prestigePoints());
  }

  @Test
  public void testDecrementPrestigePoints() {
    hand.setPrestigePoints(8);
    assertEquals(8, hand.prestigePoints());
    hand.decrementPrestigePoints(2);
    assertEquals(6, hand.prestigePoints());
  }

  @Test
  public void testEquals() {
    Hand handToTest = new Hand(0, null, null, null, null, null, null);
    Hand equalHand = new Hand(0, null, null, null, null, null, null);
    assertNotEquals(null, handToTest);
    assertNotEquals(handToTest, new Object());
    assertNotEquals(handToTest, new Object());
    assertEquals(handToTest, handToTest);
    assertEquals(handToTest, equalHand);
  }
}