package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.boards.GemColor;
import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Gems form.
 */
public final class GemsForm extends HashMap<GemColor, Integer> {

  private static final GemColor[] CONVERSION_ARRAY =
      {GemColor.GREEN, GemColor.WHITE, GemColor.BLUE, GemColor.BLACK, GemColor.RED, GemColor.GOLD};

  /**
   * Create a new empty gems form.
   */
  public GemsForm() {
    super();
  }

  /**
   * Creates shallow copy of gems form.
   *
   * @param gemsForm gems form.
   */
  public GemsForm(GemsForm gemsForm) {
    super(gemsForm);
  }

  /**
   * Converts a GemsForm representing cost (which excludes GOLD) into a 5-int array [0,0,0,0,0]
   *
   * @param gemsForm form to convert
   * @return array of cost values
   */
  public static int[] costHashToArray(GemsForm gemsForm) {
    if (gemsForm == null) {
      throw new InvalidParameterException("gemsForm cannot be null");
    }

    int[] cost = new int[5];

    for (int i = 0; i < cost.length; i++) {
      cost[i] = (gemsForm.get(CONVERSION_ARRAY[i]) == null) ? 0 :
          gemsForm.get(CONVERSION_ARRAY[i]).intValue();
    }

    return cost;
  }

  /**
   * Converts a GemsForm representing cost (which includes GOLD) into a 6-int array [0,0,0,0,0,0]
   *
   * @param gemsForm form to convert
   * @return array of cost values
   */
  public static int[] costHashToArrayWithGold(GemsForm gemsForm) {
    if (gemsForm == null) {
      throw new InvalidParameterException("gemsForm cannot be null");
    }

    int[] cost = new int[6];

    for (int i = 0; i < cost.length; i++) {
      cost[i] = (gemsForm.get(CONVERSION_ARRAY[i]) == null) ? 0 :
          gemsForm.get(CONVERSION_ARRAY[i]).intValue();
    }

    return cost;
  }

  /**
   * Converts an array representing cost into a GemsForm
   *
   * @param cost array to convert
   * @return GemsForm object with converted cost values
   */
  public static GemsForm costArrayToHash(int[] cost) {
    if (cost == null) {
      throw new InvalidParameterException("cost cannot be null");
    }
    if (cost.length < 6) {
      throw new InvalidParameterException("Cost array must be less than 7 long");
    }

    GemsForm convertedForm = new GemsForm();

    for (int i = 0; i < cost.length; i++) {
      final int curCost = cost[i];
      convertedForm.computeIfAbsent(CONVERSION_ARRAY[i], k -> curCost > 0 ? curCost : null);
    }

    return convertedForm;
  }

  /**
   * Get the discounted cost from a card's cost.
   *
   * @param gemDiscounts the player's gem discounts
   * @return The discounted cost
   */
  public GemsForm getDiscountedCost(GemsForm gemDiscounts) {
    final GemsForm result = new GemsForm(this);

    // Remove the gem discounts from the cost of the card to get the cost for this specific player
    result.removeGems(gemDiscounts);

    return result;
  }

  /**
   * Remove gems from other gems.
   *
   * @param gemsToRemove the gems to remove
   */
  public void removeGems(GemsForm gemsToRemove) {
    gemsToRemove.forEach((key, amountToRemove) -> this.computeIfPresent(key,
        (k, v) -> amountToRemove >= v ? null : v - amountToRemove));
  }

  /**
   * Add gems to <b>this</b>.
   *
   * @param gemsToAdd the gems to add
   */
  public void addGems(GemsForm gemsToAdd) {
    gemsToAdd.forEach((key, value) -> this.merge(key, value, Integer::sum));
  }

  /**
   * Count the total amount of gems.
   *
   * @return the number of gems.
   */
  public int count() {
    return this.values().stream().mapToInt(value -> value).sum();
  }
}
