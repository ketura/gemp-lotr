package com.gempukku.lotro.common;

public enum Keyword {
    SUPPORT_AREA("Support Area"),

    SKIRMISH("Skirmish"), FELLOWSHIP("Fellowship"), RESPONSE("Response"), MANEUVER("Maneuver"), ARCHERY("Archery"), SHADOW("Shadow"), ASSIGNMENT("Assignment"), REGROUP("Regroup"),

    RING_BOUND("Ring-Bound", true), RING_BEARER("Ring-Bearer", true),

    ROAMING("Roaming", true),

    WEATHER("Weather"), TALE("Tale"), SPELL("Spell"), SEARCH("Search"), STEALTH("Stealth"),

    RIVER("River"), PLAINS("Plains"), UNDERGROUND("Underground"), SANCTUARY("Sanctuary"), FOREST("Forest"), MARSH("Marsh"), MOUNTAIN("Mountain"),

    DAMAGE("Damage", true, true), DEFENDER("Defender", true, true), FIERCE("Fierce", true), ARCHER("Archer", true), RANGER("Ranger", true), TRACKER("Tracker", true),

    HAND_WEAPON("Hand Weapon"), ARMOR("Armor"), HELM("Helm"), MOUNT("Mount"), RANGED_WEAPON("Ranged Weapon"), CLOAK("Cloak"), PIPE("Pipe"),
    PIPEWEED("Pipeweed"), SHIELD("Shield");

    private String _humanReadable;
    private boolean _infoDisplayable;
    private boolean _multiples;

    private Keyword(String humanReadable) {
        this(humanReadable, false);
    }

    private Keyword(String humanReadable, boolean infoDisplayable) {
        this(humanReadable, infoDisplayable, false);
    }

    private Keyword(String humanReadable, boolean infoDisplayable, boolean multiples) {
        _humanReadable = humanReadable;
        _infoDisplayable = infoDisplayable;
        _multiples = multiples;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }

    public boolean isInfoDisplayable() {
        return _infoDisplayable;
    }

    public boolean isMultiples() {
        return _multiples;
    }
}
