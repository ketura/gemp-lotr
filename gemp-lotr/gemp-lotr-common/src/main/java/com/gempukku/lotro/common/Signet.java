package com.gempukku.lotro.common;

public enum Signet implements Filterable {
    ARAGORN("Aragorn"), FRODO("Frodo"), GANDALF("Gandalf"), THEODEN("Théoden");

    private String displayText;

    Signet(String displayText) {
        this.displayText = displayText;
    }

    public String toString() {
        return displayText;
    }
}
