package com.gempukku.lotro.common;

public enum Culture implements Filterable {
    DWARVEN("Dwarven"), ELVEN("Elven"), GANDALF("Gandalf"), GOLLUM("Gollum"), GONDOR("Gondor"), ROHAN("Rohan"), SHIRE("Shire"),
    DUNLAND("Dunland"), ISENGARD("Isengard"), MEN("Men"), MORIA("Moria"), ORC("Orc"), RAIDER("Raider"), SAURON("Sauron"), WRAITH("Wraith");

    private String _humanReadable;

    private Culture(String humanReadable) {
        _humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }
}
