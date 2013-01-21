package com.gempukku.lotro.common;

public enum Culture implements Filterable {
    DWARVEN("Dwarven"), ELVEN("Elven"), GANDALF("Gandalf"), GOLLUM("Gollum"), GONDOR("Gondor"), ROHAN("Rohan"), SHIRE("Shire"),
    DUNLAND("Dunland"), ISENGARD("Isengard"), MEN("Men"), MORIA("Moria"), ORC("Orc"), RAIDER("Raider"), SAURON("Sauron"), URUK_HAI("Uruk-hai"), WRAITH("Wraith"),
    FALLEN_REALMS("Fallen Realms", false);

    private String _humanReadable;
    private boolean _official;

    private Culture(String humanReadable) {
        this(humanReadable, true);
    }

    private Culture(String humanReadable, boolean official) {
        _humanReadable = humanReadable;
        _official = official;
    }

    public boolean isOfficial() {
        return _official;
    }

    public String getHumanReadable() {
        return _humanReadable;
    }

    public static Culture findCultureByHumanReadable(String humanReadable) {
        for (Culture culture : values()) {
            if (culture.getHumanReadable().equals(humanReadable))
                return culture;
        }
        return null;
    }
}
