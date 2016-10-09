package com.gempukku.lotro.common;

public enum Race implements Filterable {
    BALROG("Balrog"), CREATURE("Creature"), WRAITH("Wraith"),
    ELF("Elf"), HOBBIT("Hobbit"), DWARF("Dwarf"), MAN("Man"), WIZARD("Wizard"), TREE("Tree"),
    URUK_HAI("Uruk-Hai"), NAZGUL("Nazgul"), ORC("Orc"), TROLL("Troll"), HALF_TROLL("Half-troll"), ENT("Ent"), SPIDER("Spider"), MAIA("Maia"),
    GOBLIN("Goblin", false), 
	
	//Additional Hobbit Draft races
	DRAGON("Dragon");

    private String _humanReadable;
    private boolean _official;

    private Race(String humanReadable) {
        this(humanReadable, true);
    }

    private Race(String humanReadable, boolean official) {
        _humanReadable = humanReadable;
        _official = official;
    }

    public boolean isOfficial() {
        return _official;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }

    public static Race findRaceByHumanReadable(String humanReadable) {
        for (Race race : values()) {
            if (race.getHumanReadable().equals(humanReadable))
                return race;
        }
        return null;
    }
}
