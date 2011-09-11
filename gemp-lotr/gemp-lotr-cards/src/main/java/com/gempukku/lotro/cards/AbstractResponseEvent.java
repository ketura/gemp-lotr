package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public abstract class AbstractResponseEvent extends AbstractEvent {
    public AbstractResponseEvent(Side side, Culture culture, String name) {
        super(side, culture, name, (Phase) null);
    }

    @Override
    public final PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return null;
    }
}
