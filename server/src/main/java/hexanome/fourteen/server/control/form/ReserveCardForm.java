package hexanome.fourteen.server.control.form;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.gem.GemColor;

/**
 * Reserve card form.
 */
public final class ReserveCardForm {
  private Card card;
  private GemColor gemColor;

  /**
   * Constructor.
   *
   * @param card     card to be reserved.
   * @param gemColor the singular gem to be discarded
   *                 (optional, only applies if the player has 10 gems).
   */
  public ReserveCardForm(Card card, GemColor gemColor) {
    this.card = card;
    this.gemColor = gemColor;
  }

  /**
   * No args constructor.
   */
  public ReserveCardForm() {

  }

  /**
   * A Getter for Card.
   *
   * @return Card
   */
  public Card card() {
    return card;
  }

  /**
   * A Getter for GemColor.
   *
   * @return the gem color
   */
  public GemColor gemColor() {
    return gemColor;
  }
}
