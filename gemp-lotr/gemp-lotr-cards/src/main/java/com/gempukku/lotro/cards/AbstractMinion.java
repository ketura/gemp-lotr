package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.*;

public class AbstractMinion extends AbstractPermanent {
    private int _strength;
    private int _vitality;
    private int _site;
    private Race _race;

    public AbstractMinion(int twilightCost, int strength, int vitality, int site, Race race, Culture culture, String name) {
        this(twilightCost, strength, vitality, site, race, culture, name, false);
    }

    public AbstractMinion(int twilightCost, int strength, int vitality, int site, Race race, Culture culture, String name, boolean unique) {
        super(Side.SHADOW, twilightCost, CardType.MINION, culture, Zone.SHADOW_CHARACTERS, name, unique);
        _strength = strength;
        _vitality = vitality;
        _site = site;
        _race = race;
    }

    @Override
    public int getStrength() {
        return _strength;
    }

    @Override
    public int getVitality() {
        return _vitality;
    }

    @Override
    public int getSiteNumber() {
        return _site;
    }

    public Race getRace() {
        return _race;
    }
}
