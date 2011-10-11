package com.gempukku.lotro.common;

public enum Zone {
    FREE_CHARACTERS("play", true), SUPPORT("play", true), SHADOW_CHARACTERS("play", true),
    ADVENTURE_PATH("play", true),
    HAND("hand", false), DECK("deck", false), ATTACHED("play", true), STACKED("stacked", false),
    DISCARD("discard", false), DEAD("dead pile", false);

    private String _humanReadable;
    private boolean _inPlay;

    private Zone(String humanReadable, boolean inPlay) {
        _humanReadable = humanReadable;
        _inPlay = inPlay;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }

    public boolean isInPlay() {
        return _inPlay;
    }
}
