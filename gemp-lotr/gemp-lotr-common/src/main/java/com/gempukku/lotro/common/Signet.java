package com.gempukku.lotro.common;

public enum Signet implements Filterable {
    ARAGORN("Aragorn"), FRODO("Frodo"), GANDALF("Gandalf"), THEODEN("Théoden");

    private final String displayText;

    Signet(String displayText) {
        this.displayText = displayText;
    }

    public String toString() {
        return displayText;
    }

    public static Signet findSignet(String name) {
        String nameCaps = name.toUpperCase().replace(" ", "");
        String nameLower = name.toLowerCase();

        for (Signet signet : values()) {
            if (signet.toString().toLowerCase().equals(nameLower) || signet.toString().equals(nameCaps))
                return signet;
        }
        return null;
    }
}
