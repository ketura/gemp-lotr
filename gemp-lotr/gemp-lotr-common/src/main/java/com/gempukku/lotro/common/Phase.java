package com.gempukku.lotro.common;

public enum Phase {
    PUT_RING_BEARER("Put Ring-bearer"), PLAY_STARTING_FELLOWSHIP("Play starting fellowship"), FELLOWSHIP("Fellowship"), SHADOW("Shadow"), MANEUVER("Maneuver"), ARCHERY("Archery"), ASSIGNMENT("Assignment"), SKIRMISH("Skirmish"), REGROUP("Regroup"), BETWEEN_TURNS("Between turns");

    private String _humanReadable;

    private Phase(String humanReadable) {
        _humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }
}
