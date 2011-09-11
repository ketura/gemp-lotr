package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;

public class OptionalTriggerAction extends DefaultCostToEffectAction {
    public OptionalTriggerAction(PhysicalCard physicalCard, Keyword type, String actionText) {
        super(physicalCard, type, getActionText(physicalCard));
    }

    private static String getActionText(PhysicalCard card) {
        return "Activated action from " + card.getBlueprint().getName();
    }
}
