package com.gempukku.lotro.cards;

public class CardNotFoundException extends Exception {
    public CardNotFoundException(String blueprint) {
        super(blueprint);
    }
}
