package com.gempukku.lotro.common;

public enum Phase {
    PUT_RING_BEARER(null), PLAY_STARTING_FELLOWSHIP(null), FELLOWSHIP("Fellowship"), SHADOW("Shadow"), MANEUVER("Maneuver"), ARCHERY("Archery"), ASSIGNMENT("Assignment"), SKIRMISH("Skirmish"), REGROUP("Regroup"), BETWEEN_TURNS(null);

    private String _humanReadable;

    private Phase(String humanReadable) {
        _humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }
}
