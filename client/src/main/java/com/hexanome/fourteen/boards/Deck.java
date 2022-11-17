package com.hexanome.fourteen.boards;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Stack;

public class Deck extends Stack<Card> {

    private final int aLevel;
    private final Expansions aExpansion;

    public Deck(int pLevel, Expansions pExpansion) {
        if(pLevel < 0 || pLevel > 3){
            throw new InvalidParameterException("Card level must be within (0,3)");
        }
        aLevel = pLevel;
        aExpansion = pExpansion;
    }
}

