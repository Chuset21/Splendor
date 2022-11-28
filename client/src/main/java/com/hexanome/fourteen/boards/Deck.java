package com.hexanome.fourteen.boards;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Stack;
import javafx.scene.image.ImageView;

/**
 * A Class the implements the functionality of a Deck of Cards.
 */
public class Deck extends Stack<Card> {

  private final int level;
  private final Expansion expansions;
  private final ArrayList<ImageView> faceUpCardSlots;

  /**
   * Constructor that makes our Deck of Cards.
   *
   * @param level           Each corresponding CardLevel
   * @param expansions      The Expansions being played
   * @param faceUpCardSlots The Cards that will be face up on the GameBoard
   */
  public Deck(int level, Expansion expansions, ArrayList<ImageView> faceUpCardSlots) {
    if (level < 0 || level > 3) {
      throw new InvalidParameterException("Card level must be within (0,3)");
    }
    if (expansions == null || faceUpCardSlots == null) {
      throw new InvalidParameterException(
          "Card must have a non-null expansion and must have a face up card slots");
    }
    this.level = level;
    this.expansions = expansions;
    this.faceUpCardSlots = faceUpCardSlots;
  }

  public int getLevel() {
    return level;
  }

  public Expansion getExpansions() {
    return expansions;
  }

  /**
   * Checks whether the given image view is contained within the decks face up card views.
   *
   * @param imageView image view being checked for
   * @return true if imageView is contained, false if not
   */
  public boolean hasCardSlot(ImageView imageView) {
    // Check every card slot in deck
    for (ImageView iv : faceUpCardSlots) {
      if (iv == imageView) {
        return true;
      }
    }

    return false;
  }

  @Override
  public String toString() {
    return "Level " + level + " deck: " + super.toString();
  }
}

