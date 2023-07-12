package com.gempukku.lotro.cards.cardtype;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Side;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractAttachable extends AbstractLotroCardBlueprint {
    private Set<PossessionClass> _possessionClasses;

    public AbstractAttachable(Side side, CardType cardType, int twilight, Culture culture, PossessionClass possessionClass, String name) {
        this(side, cardType, twilight, culture, possessionClass, name, null, false);
    }

    public AbstractAttachable(Side side, CardType cardType, int twilight, Culture culture, PossessionClass possessionClass, String name, String subTitle, boolean unique) {
        super(twilight, side, cardType, culture, name, subTitle, unique);
        if (possessionClass != null)
            _possessionClasses = Collections.singleton(possessionClass);
    }

    @Override
    public Set<PossessionClass> getPossessionClasses() {
        return _possessionClasses;
    }
}
