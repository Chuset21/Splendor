package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.boards.GemColor;
import java.security.InvalidParameterException;
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
   * Converts a GemsForm representing cost (which excludes GOLD) into a 5-int array [0,0,0,0,0]
   *
   * @param gemsForm form to convert
   * @return array of cost values
   */
  public static int[] costHashToArray(GemsForm gemsForm){
    if(gemsForm == null){
      throw new InvalidParameterException("gemsForm cannot be null");
    }

    int[] cost = new int[5];

    for(int i = 0;i<cost.length;i++){
      cost[i] = (gemsForm.get(CONVERSION_ARRAY[i]) == null) ? 0 : gemsForm.get(CONVERSION_ARRAY[i]).intValue();
    }

    return cost;
  }

  /**
   * Converts an array representing cost (which excludes GOLD) into a GemsForm
   *
   * @param cost array to convert
   * @return GemsForm object with converted cost values
   */
  public static GemsForm costArrayToHash(int[] cost){
    if(cost == null){
      throw new InvalidParameterException("cost cannot be null");
    }
    if(cost.length != 5){
      throw new InvalidParameterException("Cost array must be of size 5, must exclude GOLD tokens");
    }

    GemsForm convertedForm = new GemsForm();

    for(int i = 0; i<5;i++){
      final int curCost = cost[i];
      convertedForm.computeIfAbsent(CONVERSION_ARRAY[i], k -> curCost > 0 ? curCost : null);
    }

    return convertedForm;
  }
}
