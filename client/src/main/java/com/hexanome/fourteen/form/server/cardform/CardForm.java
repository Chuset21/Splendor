package com.hexanome.fourteen.form.server.cardform;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.form.server.GemsForm;

/**
 * Card form.
 */
public abstract class CardForm {
  int prestigePoints;
  GemsForm cost;
  CardLevelForm level;
  Expansion expansion;

  /**
   * Constructor.
   *
   * @param prestigePoints the amount of prestige points associated with the card
   * @param cost           the cost of the card
   * @param level          the level of the card
   * @param expansion      the expansion to which the card belongs to
   */
  public CardForm(int prestigePoints, GemsForm cost, CardLevelForm level, Expansion expansion) {
    this.prestigePoints = prestigePoints;
    this.cost = cost;
    this.level = level;
    this.expansion = expansion;
  }

  /**
   * No args constructor.
   */
  public CardForm() {
  }

  /**
   * A Getter to get the Prestige Points of the Card.
   *
   * @return Prestige Points of the Card
   */
  public int prestigePoints() {
    return prestigePoints;
  }

  /**
   * A Getter to get the Cost of the Card.
   *
   * @return Cost of the Card
   */
  public GemsForm cost() {
    return cost;
  }

  /**
   * A Getter to get the Level of the Card.
   *
   * @return Level of the Card
   */
  public CardLevelForm level() {
    return level;
  }

  /**
   * A Getter to get which Expansion in which the Card is a part of.
   *
   * @return Expansion of the Card
   */
  public Expansion expansion() {
    return expansion;
  }
}
