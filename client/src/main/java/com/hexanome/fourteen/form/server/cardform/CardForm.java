package com.hexanome.fourteen.form.server.cardform;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.boards.GemColor;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.PlayerForm;
import com.hexanome.fourteen.form.server.tradingposts.TradingPostsEnum;

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

  /**
   * Determines if the player can pay for given card
   *
   * @param playerForm player's info to cross-reference
   * @return boolean representing affordability
   */
  public boolean isAffordable(PlayerForm playerForm) {
    if (cost == null) {
      return true;
    }

    GemsForm tempCost = new GemsForm(cost);
    tempCost.removeGems(playerForm.hand().gems());
    tempCost.removeGems(playerForm.hand().gemDiscounts());

    int[] tempArrayCost = GemsForm.costHashToArray(tempCost);

    int goldGems = playerForm.hand().getNumGoldGemCards() * 2 +
        playerForm.hand().gems().getOrDefault(GemColor.GOLD, 0).intValue();

    boolean doubleGoldTradingPostActive =
        playerForm.hand().tradingPosts().getOrDefault(TradingPostsEnum.DOUBLE_GOLD_GEMS, false);
    boolean isAffordable = true;

    for (int i : tempArrayCost) {
      if (i <= goldGems * (doubleGoldTradingPostActive ? 2 : 1)) {
        // Subtract # of gold gems
        if (doubleGoldTradingPostActive) {
          goldGems -= i/2 + i%2 == 1 ? 1 : 0;
        } else {
          goldGems -= i;
        }
      } else {
        // Gold cannot cover cost
        isAffordable = false;
        break;
      }
    }

    return isAffordable;
  }

  /**
   * Determines if the player can pay for given card
   *
   * @param playerForm player's info to cross-reference
   * @return boolean representing affordability
   */
  public boolean canUseGoldGemCards(PlayerForm playerForm) {
    if (this.cost() == null) {
      return true;
    }

    GemsForm tempCost = new GemsForm(this.cost());
    tempCost.removeGems(playerForm.hand().gemDiscounts());
    return 0 < tempCost.count() &&
        (0 < playerForm.hand().gems().getOrDefault(GemColor.GOLD, 0).intValue() ||
            0 < playerForm.hand().getNumGoldGemCards());
  }
}
