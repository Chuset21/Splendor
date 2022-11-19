package com.hexanome.fourteen.boards;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Stack;

public class Deck extends Stack<Card> {

    private final int aLevel;
    private final Expansions aExpansion;
    private final ArrayList<ImageView> aFaceUpCardSlots;

    public Deck(int pLevel, Expansions pExpansion, ArrayList<ImageView> pFaceUpCardSlots) {
        if(pLevel < 0 || pLevel > 3){
            throw new InvalidParameterException("Card level must be within (0,3)");
        }
        if(pExpansion == null || pFaceUpCardSlots == null){
            throw new InvalidParameterException("Card must have a non-null expansion and must have a face up card slots");
        }
        aLevel = pLevel;
        aExpansion = pExpansion;
        aFaceUpCardSlots = pFaceUpCardSlots;
    }

    public int getLevel() {
        return aLevel;
    }

    public Expansions getExpansion() {
        return aExpansion;
    }

    /**
     * Checks whether the given image view is contained within the decks face up card views
     *
     * @param imageView image view being checked for
     * @return true if imageView is contained, false if not
     */
    public boolean hasCardSlot(ImageView imageView){
        // Check every card slot in deck
        for(ImageView iv : aFaceUpCardSlots){
            if(iv == imageView){
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Level "+aLevel+" deck: "+super.toString();
    }
}

