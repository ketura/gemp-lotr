package com.gempukku.lotro.common;

public enum Block {
    FELLOWSHIP("Fellowship"), TWO_TOWERS("Towers"), KING("King"), SHADOWS("Shadows");

    private String _humanReadable;

    private Block(String humanReadable) {
        _humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }
}
