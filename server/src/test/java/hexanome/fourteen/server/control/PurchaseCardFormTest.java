package hexanome.fourteen.server.control;

import hexanome.fourteen.server.control.form.PurchaseCardForm;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.card.CardLevel;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.Gems;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
public class PurchaseCardFormTest {

  static Card card;
  static Gems chosenGems;
  static Gems substitutedGems;

  static PurchaseCardForm purchasedCard;

  @BeforeAll
  public static void setUp() {
    card = new Card() {
      @Override
      public int prestigePoints() {
        return super.prestigePoints();
      }

      @Override
      public Gems cost() {
        return super.cost();
      }

      @Override
      public CardLevel level() {
        return super.level();
      }

      @Override
      public Expansion expansion() {
        return super.expansion();
      }
    };

    chosenGems = new Gems();

    substitutedGems = new Gems();

    purchasedCard = new PurchaseCardForm(card, chosenGems, substitutedGems);
  }

  @Test
  public void testNoArgsConstructor() {
    PurchaseCardForm expectedCard = new PurchaseCardForm();

    Assertions.assertNull(expectedCard.card());
    Assertions.assertNull(expectedCard.gemsToPayWith());
    Assertions.assertNull(expectedCard.substitutedGems());
  }


  @Test
  public void testCard() {
    Assertions.assertEquals(card, purchasedCard.card());
  }

  @Test
  public void testGemsToPayWith() {
    Assertions.assertEquals(chosenGems, purchasedCard.gemsToPayWith());
  }

  @Test
  public void testSubstitutedGems() {
    Assertions.assertEquals(substitutedGems, purchasedCard.substitutedGems());
  }
}