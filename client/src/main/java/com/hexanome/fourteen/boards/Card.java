package com.hexanome.fourteen.boards;

import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Objects;

public class Card {

    private final HashMap<GemColors, Integer> aCost;
    private final GemColors aDiscountColor;
    private final int aDiscountAmt;
    private final Expansions aExpansion;
    private final int aLevel;
    private final int aVictoryPoints;
    private final ImageView aCardImage;

    public Card(HashMap<GemColors, Integer> pCost, GemColors pDiscountColor, int pDiscountAmt, Expansions pExpansion, int pLevel, int pVictoryPoints, ImageView pCardImage){
        aCost = new HashMap<>(pCost);
        aDiscountColor = pDiscountColor;
        aDiscountAmt = pDiscountAmt;
        aExpansion = pExpansion;
        aLevel = pLevel;
        aVictoryPoints = pVictoryPoints;
        aCardImage = pCardImage;
    }

    public static void setupCards() {
        //System.out.println(Objects.requireNonNull(OrientExpansion.class.getResource("*.png")));
    }
}
