package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.boards.GemColor;
import java.util.HashMap;

/**
 * Gems form.
 */
public final class GemsForm extends HashMap<GemColor, Integer> {

  private static final GemColor[] CONVERSION_ARRAY =
      {GemColor.GREEN, GemColor.WHITE, GemColor.BLUE, GemColor.BLACK, GemColor.RED};

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
   * Converts a GemsForm representing cost (which excludes GOLD) into a 5-int array
   *
   * @param gemsForm form to convert
   * @return array of cost values
   */
  public static int[] costHashToArray(GemsForm gemsForm){
    int[] cost = new int[5];

    for(int i = 0;i<cost.length;i++){
      cost[i] = (gemsForm.get(CONVERSION_ARRAY[i]) == null) ? 0 : gemsForm.get(CONVERSION_ARRAY[i]).intValue();
    }

    return cost;
  }
}
