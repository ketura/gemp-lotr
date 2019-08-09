package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;

public class AbstractResponseEvent extends AbstractEvent {
    public AbstractResponseEvent(Side side, int twilightCost, Culture culture, String name) {
        super(side, twilightCost, culture, name, null);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return null;
    }
}
