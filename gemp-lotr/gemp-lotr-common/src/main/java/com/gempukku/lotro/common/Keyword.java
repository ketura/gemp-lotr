package com.gempukku.lotro.common;

public enum Keyword {
    ELF("Elf"), HOBBIT("Hobbit"), DWARF("Dwarf"), MAN("Man"), WIZARD("Wizard"),
    URUK_HAI("Uruk-Hai"), NAZGUL("Nazgul"), ORC("Orc"), TROLL("Troll"),

    SUPPORT_AREA("Support Area"),

    SKIRMISH("Skirmish"), FELLOWSHIP("Fellowship"), RESPONSE("Response"), MANEUVER("Maneuver"), ARCHERY("Archery"), SHADOW("Shadow"), ASSIGNMENT("Assignment"), REGROUP("Regroup"),

    RING_BOUND("Ring-Bound"), RING_BEARER("Ring-Bearer"),

    ROAMING("Roaming"),

    WEATHER("Weather"), TALE("Tale"), SPELL("Spell"), SEARCH("Search"), STEALTH("Stealth"),

    RIVER("River"), PLAINS("Plains"), UNDERGROUND("Underground"), SANCTUARY("Sanctuary"), FOREST("Forest"), MARSH("Marsh"), MOUNTAIN("Mountain"),

    DAMAGE("Damage"), DEFENDER("Defender"), FIERCE("Fierce"), ARCHER("Archer"), RANGER("Ranger"), TRACKER("Tracker"),

    HAND_WEAPON("Hand Weapon"), ARMOR("Armor"), HELM("Helm"), MOUNT("Mount"), RANGED_WEAPON("Ranged Weapon"), CLOAK("Cloak"), PIPE("Pipe"),
    PIPEWEED("Pipeweed"), SHIELD("Shield");

    private String _humanReadable;

    private Keyword(String humanReadable) {
        _humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }
}
