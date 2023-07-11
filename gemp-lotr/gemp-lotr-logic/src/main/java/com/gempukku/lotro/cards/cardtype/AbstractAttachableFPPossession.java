package com.gempukku.lotro.cards.cardtype;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Side;

public abstract class AbstractAttachableFPPossession extends AbstractAttachable {
    private final int _strength;
    private final int _vitality;

    public AbstractAttachableFPPossession(int twilight, int strength, int vitality, Culture culture, PossessionClass possessionClass, String name) {
        this(twilight, strength, vitality, culture, possessionClass, name, null, false);
    }

    public AbstractAttachableFPPossession(int twilight, int strength, int vitality, Culture culture, PossessionClass possessionClass, String name, String subTitle, boolean unique) {
        this(twilight, strength, vitality, culture, CardType.POSSESSION, possessionClass, name, subTitle, unique);
    }

    public AbstractAttachableFPPossession(int twilight, int strength, int vitality, Culture culture, CardType cardType, PossessionClass possessionClass, String name, String subTitle, boolean unique) {
        super(Side.FREE_PEOPLE, cardType, twilight, culture, possessionClass, name, subTitle, unique);
        _strength = strength;
        _vitality = vitality;
    }

    @Override
    public int getStrength() {
        return _strength;
    }

    @Override
    public int getVitality() {
        return _vitality;
    }
}
