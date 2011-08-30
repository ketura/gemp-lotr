package com.gempukku.lotro.common;

public enum Keyword {
    ELF("Elf"), HOBBIT("Hobbit"), DWARF("Dwarf"), MAN("Man"),
    URUK_HAI("Uruk-Hai"), NAZGUL("Nazgul"), ORC("Orc"),

    SUPPORT_AREA("Support Area"), SKIRMISH("Skirmish"), FELLOWSHIP("Fellowship"), RESPONSE("Response"), MANEUVER("Maneuver"), ARCHERY("Archery"),

    RING_BOUND("Ring-Bound"), RING_BEARER("Ring-Bearer"),

    ROAMING("Roaming"),

    WEATHER("Weather"), TALE("Tale"), SPELL("Spell"),

    RIVER("River"), PLAINS("Plains"), UNDERGROUND("Underground"), SANCTUARY("Sanctuary"),

    DAMAGE("Damage"), DEFENDER("Defender"), FIERCE("Fierce"), ARCHER("Archer"), RANGER("Ranger"),

    HAND_WEAPON("Hand Weapon"), ARMOR("Armor"), HELM("Helm"), MOUNT("Mount"), RANGED_WEAPON("Ranged Weapon"), CLOAK("Cloak");

    private String _humanReadable;

    private Keyword(String humanReadable) {
        _humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }
}
