package hexanome.fourteen.server.model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class HandTest {
  static Hand hand;

  @BeforeEach
  public void setUp() {
    hand = new Hand();
  }

  @Test
  public void testConstructor() {
    Hand handToTest = new Hand(0, null, null, null, null, null, null);
    assertEquals(0, handToTest.prestigePoints());
    assertNull(handToTest.gems());
    assertNull(handToTest.reservedCards());
    assertNull(handToTest.purchasedCards());
    assertNull(handToTest.visitedNoble());
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
  public void testSetReservedCards() {
    hand.setReservedCards(null);
    assertNull(hand.reservedCards());
  }

  @Test
  public void testSetPurchasedCards() {
    hand.setPurchasedCards(null);
    assertNull(hand.purchasedCards());
  }

  @Test
  public void testSetVisitedNoble() {
    hand.setVisitedNoble(null);
    assertNull(hand.visitedNoble());
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
    assertFalse(handToTest.equals(null));
    assertFalse(handToTest.equals(new Object()));
    assertFalse(handToTest.equals(new Object()));
    assertTrue(handToTest.equals(handToTest));
    assertTrue(handToTest.equals(equalHand));
  }
}