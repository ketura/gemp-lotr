package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;

public class RequiredTriggerAction extends DefaultCostToEffectAction {
    public RequiredTriggerAction(PhysicalCard physicalCard, Keyword type, String actionText) {
        super(physicalCard, type, getActionText(physicalCard));
    }

    private static String getActionText(PhysicalCard card) {
        if (card == null)
            return "Required rules action";
        else
            return "Required action from " + card.getBlueprint().getName();
    }
}
