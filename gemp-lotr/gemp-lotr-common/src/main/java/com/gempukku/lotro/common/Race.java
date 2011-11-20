package com.gempukku.lotro.common;

public enum Race implements Filterable {
    BALROG("Balrog"), CREATURE("Creature"), WRAITH("Wraith"),
    ELF("Elf"), HOBBIT("Hobbit"), DWARF("Dwarf"), MAN("Man"), WIZARD("Wizard"), TREE("Tree"),
    URUK_HAI("Uruk-Hai"), NAZGUL("Nazgul"), ORC("Orc"), TROLL("Troll"), ENT("Ent"), SPIDER("Spider"), MAIA("Maia");

    private String _humanReadable;

    private Race(String humanReadable) {
        _humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }
}
