package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.PhysicalCard;

public class RequiredTriggerAction extends ActivateCardAction {
    private PhysicalCard _physicalCard;

    public RequiredTriggerAction(PhysicalCard physicalCard) {
        super(physicalCard, null);
        _physicalCard = physicalCard;
    }

    @Override
    protected String getMessage() {
        return "<div class='cardHint' value='" + _physicalCard.getBlueprintId() + "'>" + _physicalCard.getBlueprint().getName() + "</div> required triggered effect is used";
    }
}
