package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;

public class RequiredTriggerAction extends ActivateCardAction {
    private PhysicalCard _physicalCard;

    public RequiredTriggerAction(PhysicalCard physicalCard, Keyword type, String actionText) {
        super(physicalCard, type, getActionText(physicalCard));
        _physicalCard = physicalCard;
    }

    private static String getActionText(PhysicalCard card) {
        if (card == null)
            return "Required rules action";
        else
            return "Required action from " + card.getBlueprint().getName();
    }

    @Override
    protected String getMessage() {
        return _physicalCard.getBlueprint().getName() + " required triggered effect is used";
    }
}
