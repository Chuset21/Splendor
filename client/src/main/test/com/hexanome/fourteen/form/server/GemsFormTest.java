package com.hexanome.fourteen.form.server;

import static org.junit.jupiter.api.Assertions.*;

import com.hexanome.fourteen.boards.GemColor;
import java.util.Arrays;

class GemsFormTest {

  private int[] testArray;
  private GemsForm testForm;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    testArray = new int[5];
    testArray[0] = 3;
    testArray[1] = 0;
    testArray[2] = 3;
    testArray[3] = 0;
    testArray[4] = 0;

    testForm = new GemsForm();

    final int greenCost = testArray[0];
    testForm.computeIfAbsent(GemColor.GREEN, k -> greenCost > 0 ? greenCost : null);

    final int whiteCost = testArray[1];
    testForm.computeIfAbsent(GemColor.WHITE, k -> whiteCost > 0 ? whiteCost : null);

    final int blueCost = testArray[2];
    testForm.computeIfAbsent(GemColor.BLUE, k -> blueCost > 0 ? blueCost : null);

    final int blackCost = testArray[3];
    testForm.computeIfAbsent(GemColor.BLACK, k -> blackCost > 0 ? blackCost : null);

    final int redCost = testArray[4];
    testForm.computeIfAbsent(GemColor.RED, k -> redCost > 0 ? redCost : null);
  }

  @org.junit.jupiter.api.Test
  void costHashToArray() {
    assertTrue(Arrays.equals(GemsForm.costHashToArray(testForm),testArray));
  }

  @org.junit.jupiter.api.Test
  void costArrayToHash() {
    assertEquals(GemsForm.costArrayToHash(testArray), testForm);
  }
}