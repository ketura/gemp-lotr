package com.gempukku.lotro.common;

public enum Zone {
    FREE_CHARACTERS("play"), FREE_SUPPORT("play"), SHADOW_CHARACTERS("play"), SHADOW_SUPPORT("play"),
    ADVENTURE_PATH("play"),
    HAND("hand"), DECK("deck"), ATTACHED("play"), STACKED("stacked"),
    DISCARD("discard"), DEAD("dead pile");

    private String _humanReadable;

    private Zone(String humanReadable) {
        _humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }
}
