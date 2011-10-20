package com.gempukku.lotro.common;

public enum Token {
    BURDEN, WOUND,

    DUNLAND(Culture.DUNLAND), DWARVEN(Culture.DWARVEN), ELVEN(Culture.ELVEN), GANDALF(Culture.GANDALF),
    GONDOR(Culture.GONDOR), ISENGARD(Culture.ISENGARD), RAIDER(Culture.RAIDER), ROHAN(Culture.ROHAN), SHIRE(Culture.SHIRE);

    private Culture _culture;

    private Token() {
        this(null);
    }

    private Token(Culture culture) {
        _culture = culture;
    }

    public Culture getCulture() {
        return _culture;
    }
}
