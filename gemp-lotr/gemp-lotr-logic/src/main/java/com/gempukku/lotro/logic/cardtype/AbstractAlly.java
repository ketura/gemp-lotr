package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.*;

public class AbstractAlly extends AbstractPermanent {
    private SitesBlock _siteBlock;
    private int[] _siteNumbers;
    private int _strength;
    private int _vitality;
    private Race _race;

    public AbstractAlly(int twilight, SitesBlock siteBlock, int siteNumber, int strength, int vitality, Race race, Culture culture, String name) {
        this(twilight, siteBlock, siteNumber, strength, vitality, race, culture, name, null, false);
    }

    public AbstractAlly(int twilight, SitesBlock siteBlock, int siteNumber, int strength, int vitality, Race race, Culture culture, String name, String subTitle, boolean unique) {
        this(twilight, siteBlock, new int[]{siteNumber}, strength, vitality, race, culture, name, subTitle, unique);
    }

    public AbstractAlly(int twilight, SitesBlock siteBlock, int[] siteNumbers, int strength, int vitality, Race race, Culture culture, String name) {
        this(twilight, siteBlock, siteNumbers, strength, vitality, race, culture, name, null, false);
    }

    public AbstractAlly(int twilight, SitesBlock siteBlock, int[] siteNumbers, int strength, int vitality, Race race, Culture culture, String name, String subTitle, boolean unique) {
        super(Side.FREE_PEOPLE, twilight, CardType.ALLY, culture, name, subTitle, unique);
        _siteBlock = siteBlock;
        _siteNumbers = siteNumbers;
        _strength = strength;
        _vitality = vitality;
        _race = race;
    }

    public final Race getRace() {
        return _race;
    }

    @Override
    public int[] getAllyHomeSiteNumbers() {
        return _siteNumbers;
    }

    @Override
    public SitesBlock getAllyHomeSiteBlock() {
        return _siteBlock;
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
        return 0;
    }
}
