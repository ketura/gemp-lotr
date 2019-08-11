package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.*;

public abstract class AbstractCompanion extends AbstractPermanent {
    private int _strength;
    private int _vitality;
    private int _resistance;
    private Race _race;
    private Signet _signet;

    public AbstractCompanion(int twilightCost, int strength, int vitality, int resistance, Culture culture, Race race, Signet signet, String name) {
        this(twilightCost, strength, vitality, resistance, culture, race, signet, name, null, false);
    }

    public AbstractCompanion(int twilightCost, int strength, int vitality, int resistance, Culture culture, Race race, Signet signet, String name, String subTitle, boolean unique) {
        super(Side.FREE_PEOPLE, twilightCost, CardType.COMPANION, culture, name, subTitle, unique);
        _strength = strength;
        _vitality = vitality;
        _resistance = resistance;
        _race = race;
        _signet = signet;
    }

    public final Race getRace() {
        return _race;
    }

    @Override
    public final Signet getSignet() {
        return _signet;
    }

    @Override
    public final int getStrength() {
        return _strength;
    }

    @Override
    public final int getVitality() {
        return _vitality;
    }

    @Override
    public final int getResistance() {
        return _resistance;
    }
}
