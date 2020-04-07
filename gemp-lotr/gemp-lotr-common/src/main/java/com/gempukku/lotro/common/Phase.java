package com.gempukku.lotro.common;

public enum Phase {
    PUT_RING_BEARER("Put Ring-bearer", false),
    PLAY_STARTING_FELLOWSHIP("Play starting fellowship", false),
    FELLOWSHIP("Fellowship", true),
    SHADOW("Shadow", true),
    MANEUVER("Maneuver", true),
    ARCHERY("Archery", true),
    ASSIGNMENT("Assignment", true),
    SKIRMISH("Skirmish", true),
    REGROUP("Regroup", true),
    BETWEEN_TURNS("Between turns", false);

    private String humanReadable;
    private boolean realPhase;

    private Phase(String humanReadable, boolean realPhase) {
        this.humanReadable = humanReadable;
        this.realPhase = realPhase;
    }

    public String getHumanReadable() {
        return humanReadable;
    }

    public boolean isRealPhase() {
        return realPhase;
    }
}
