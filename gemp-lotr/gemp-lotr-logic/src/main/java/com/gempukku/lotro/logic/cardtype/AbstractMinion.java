package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.*;

public class AbstractMinion extends AbstractPermanent {
    private final int _strength;
    private final int _vitality;
    private final int _site;
    private final Race _race;

    public AbstractMinion(int twilightCost, int strength, int vitality, int site, Race race, Culture culture, String name) {
        this(twilightCost, strength, vitality, site, race, culture, name, null, false);
    }

    public AbstractMinion(int twilightCost, int strength, int vitality, int site, Race race, Culture culture, String name, String subTitle, boolean unique) {
        super(Side.SHADOW, twilightCost, CardType.MINION, culture, name, subTitle, unique);
        _strength = strength;
        _vitality = vitality;
        _site = site;
        _race = race;
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
    public final int getSiteNumber() {
        return _site;
    }

    @Override
    public final Race getRace() {
        return _race;
    }
}
