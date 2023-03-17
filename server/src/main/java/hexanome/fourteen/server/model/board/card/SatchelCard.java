package hexanome.fourteen.server.model.board.card;

import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.Gems;
import java.util.Objects;

/**
 * Satchel Card.
 */
public final class SatchelCard extends Card {
  private Card cardToAttach;
  private Card freeCardToTake;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param cardToAttach   the card to attach
   * @param freeCardToTake the card to take for free, if this card is a level two card
   */
  public SatchelCard(int prestigePoints, Gems cost, CardLevel level, Expansion expansion,
                     Card cardToAttach, Card freeCardToTake) {
    super(prestigePoints, cost, level, expansion);
    this.cardToAttach = cardToAttach;
    this.freeCardToTake = freeCardToTake;
  }

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   * @param cardToAttach   the card to attach
   */
  public SatchelCard(int prestigePoints, Gems cost, CardLevel level, Expansion expansion,
                     Card cardToAttach) {
    this(prestigePoints, cost, level, expansion, cardToAttach, null);
  }


  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   */
  public SatchelCard(int prestigePoints, Gems cost, CardLevel level, Expansion expansion) {
    this(prestigePoints, cost, level, expansion, null);
  }

  /**
   * No args constructor.
   */
  public SatchelCard() {
    super();
  }

  /**
   * Getter for card to attach.
   *
   * @return The card to attach to this satchel card.
   */
  public Card cardToAttach() {
    return cardToAttach;
  }

  /**
   * Getter for free card to take.
   *
   * @return The free card to take (will be null unless this card is a level two card)
   */
  public Card freeCardToTake() {
    return freeCardToTake;
  }

  /**
   * Remove the free card to take from this card.
   * This is intended to be used after successfully taking a free card.
   */
  public void removeFreeCardToTake() {
    freeCardToTake = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    SatchelCard card = (SatchelCard) obj;
    return super.prestigePoints == card.prestigePoints && Objects.equals(super.cost, card.cost)
           && super.level == card.level && super.expansion == card.expansion;
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cost, level, expansion);
  }
}
