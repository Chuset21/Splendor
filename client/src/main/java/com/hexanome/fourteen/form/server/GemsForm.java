package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.boards.GemColor;
import java.util.HashMap;

/**
 * Gems form.
 */
public final class GemsForm extends HashMap<GemColor, Integer> {

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
}
