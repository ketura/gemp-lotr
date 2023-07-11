package com.gempukku.lotro.cards.cardtype;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;

public abstract class AbstractFollower extends AbstractPermanent {
    private final int _strength;
    private final int _vitality;
    private final int _resistance;

    public AbstractFollower(Side side, int twilightCost, int strength, int vitality, int resistance, Culture culture, String name) {
        this(side, twilightCost, strength, vitality, resistance, culture, name, null, false);
    }

    public AbstractFollower(Side side, int twilightCost, int strength, int vitality, int resistance, Culture culture, String name, String subTitle, boolean unique) {
        super(side, twilightCost, CardType.FOLLOWER, culture, name, subTitle, unique);
        _strength = strength;
        _vitality = vitality;
        _resistance = resistance;
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
    public int getResistance() {
        return _resistance;
    }
}
