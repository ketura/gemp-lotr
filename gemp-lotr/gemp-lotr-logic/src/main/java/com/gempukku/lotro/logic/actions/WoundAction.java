package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;

public class WoundAction extends ActivateCardAction {
    public WoundAction(String playerId, PhysicalCard card, int wounds) {
        super(null, null, "Wound character");
        for (int i = 0; i < wounds; i++)
            appendEffect(new WoundCharacterEffect(playerId, card));
    }

    public WoundAction(String playerId, Filter filter, int wounds) {
        super(null, null, "Wound character(s)");
        for (int i = 0; i < wounds; i++)
            appendEffect(new WoundCharacterEffect(playerId, filter));
    }
}
