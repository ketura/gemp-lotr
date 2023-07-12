package com.gempukku.lotro.cards.cardtype;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;

public class AbstractPermanent extends AbstractLotroCardBlueprint {
    public AbstractPermanent(Side side, int twilightCost, CardType cardType, Culture culture, String name) {
        this(side, twilightCost, cardType, culture, name, null, false);
    }

    public AbstractPermanent(Side side, int twilightCost, CardType cardType, Culture culture, String name, String subTitle, boolean unique) {
        super(twilightCost, side, cardType, culture, name, subTitle, unique);
        if (cardType != CardType.COMPANION && cardType!= CardType.MINION)
            addKeyword(Keyword.SUPPORT_AREA);
    }
}
