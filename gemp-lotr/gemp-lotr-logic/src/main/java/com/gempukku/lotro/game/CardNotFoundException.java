package com.gempukku.lotro.game;

public class CardNotFoundException extends Exception {
    public CardNotFoundException(String blueprint) {
        super(blueprint);
    }
}
