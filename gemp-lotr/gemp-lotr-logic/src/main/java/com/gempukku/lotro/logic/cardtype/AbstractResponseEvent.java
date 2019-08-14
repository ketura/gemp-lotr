package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;

public class AbstractResponseEvent extends AbstractLotroCardBlueprint {
    public AbstractResponseEvent(Side side, int twilightCost, Culture culture, String name) {
        super(twilightCost, side, CardType.EVENT, culture, name);
        addKeyword(Keyword.RESPONSE);
    }
}
