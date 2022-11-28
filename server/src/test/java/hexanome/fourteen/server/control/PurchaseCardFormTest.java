package hexanome.fourteen.server.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
class PurchaseCardFormTest {

  @Test
  void noArgsConstructor() {
    PurchaseCardForm expectedCard = new PurchaseCardForm();

    Assertions.assertNull(expectedCard.card());
    Assertions.assertNull(expectedCard.gemsToPayWith());
    Assertions.assertNull(expectedCard.substitutedGems());
  }


  @Test
  void card() {
  }

  @Test
  void gemsToPayWith() {
  }

  @Test
  void substitutedGems() {
  }
}