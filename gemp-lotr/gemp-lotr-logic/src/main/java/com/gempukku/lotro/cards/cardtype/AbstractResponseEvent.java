package com.gempukku.lotro.cards.cardtype;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;

public class AbstractResponseEvent extends AbstractLotroCardBlueprint {
    public AbstractResponseEvent(Side side, int twilightCost, Culture culture, String name) {
        super(twilightCost, side, CardType.EVENT, culture, name);
        addKeyword(Keyword.RESPONSE);
    }
}
