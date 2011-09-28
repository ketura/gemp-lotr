package com.gempukku.lotro.common;

public enum Race {
    BALROG("Balrog"),
    ELF("Elf"), HOBBIT("Hobbit"), DWARF("Dwarf"), MAN("Man"), WIZARD("Wizard"),
    URUK_HAI("Uruk-Hai"), NAZGUL("Nazgul"), ORC("Orc"), TROLL("Troll");

    private String _humanReadable;

    private Race(String humanReadable) {
        _humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }
}
