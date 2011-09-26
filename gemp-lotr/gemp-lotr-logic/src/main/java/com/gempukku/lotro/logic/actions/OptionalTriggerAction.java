package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.PhysicalCard;

public class OptionalTriggerAction extends ActivateCardAction {
    private PhysicalCard _physicalCard;

    public OptionalTriggerAction(PhysicalCard physicalCard) {
        super(physicalCard, null);
        _physicalCard = physicalCard;
    }

    @Override
    protected String getMessage() {
        return _physicalCard.getBlueprint().getName() + " optional triggered effect is used";
    }
}
