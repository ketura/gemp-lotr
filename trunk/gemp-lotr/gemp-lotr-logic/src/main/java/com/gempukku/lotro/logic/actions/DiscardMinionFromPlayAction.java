package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;

public class DiscardMinionFromPlayAction extends DefaultCostToEffectAction {
    public DiscardMinionFromPlayAction(PhysicalCard card) {
        super(null, null, "Discard the minion");

        addEffect(new DiscardCardFromPlayEffect(null, card));
    }
}