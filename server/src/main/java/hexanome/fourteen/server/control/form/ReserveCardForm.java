package hexanome.fourteen.server.control.form;

import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.gem.GemColor;

/**
 * Reserve card form.
 */
public final class ReserveCardForm {
  private Card card;
  private GemColor gemColor;
  private boolean isTakingFaceDown;

  /**
   * Constructor.
   *
   * @param card             card to be reserved (if isTakingFaceDown is true this just says what
   *                         deck you're taking from, the level and the expansion).
   * @param gemColor         the singular gem to be discarded
   *                         (optional, only applies if the player has 10 gems).
   * @param isTakingFaceDown whether the player is taking a face down card or not.
   */
  public ReserveCardForm(Card card, GemColor gemColor, boolean isTakingFaceDown) {
    this.card = card;
    this.gemColor = gemColor;
    this.isTakingFaceDown = isTakingFaceDown;
  }

  /**
   * No args constructor.
   */
  public ReserveCardForm() {

  }

  /**
   * A Getter for card.
   *
   * @return Card
   */
  public Card card() {
    return card;
  }

  /**
   * A Getter for gemColor.
   *
   * @return the gem color
   */
  public GemColor gemColor() {
    return gemColor;
  }

  /**
   * A Getter for isTakingFaceDown.
   *
   * @return whether the player is taking a face down card or not.
   */
  public boolean isTakingFaceDown() {
    return isTakingFaceDown;
  }
}
