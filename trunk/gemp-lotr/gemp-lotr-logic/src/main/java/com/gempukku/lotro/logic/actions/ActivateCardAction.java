package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class ActivateCardAction extends AbstractCostToEffectAction {
    private PhysicalCard _physicalCard;
    private Keyword _type;

    private boolean _sentMessage;

    public ActivateCardAction(PhysicalCard physicalCard, Keyword type) {
        _physicalCard = physicalCard;
        _type = type;
    }

    @Override
    public Keyword getType() {
        return _type;
    }

    @Override
    public PhysicalCard getActionSource() {
        return _physicalCard;
    }

    @Override
    public String getText(LotroGame game) {
        return "Use " + _physicalCard.getBlueprint().getName();
    }

    @Override
    public Effect nextEffect() {
        if (!_sentMessage && _physicalCard != null) {
            _sentMessage = true;
            return new SendMessageEffect(getMessage());
        }

        if (!isCostFailed()) {
            Effect cost = getNextCost();
            if (cost != null)
                return cost;

            Effect effect = getNextEffect();
            if (effect != null)
                return effect;
        }
        return null;
    }

    protected String getMessage() {
        return "<div class='cardHint' value='" + _physicalCard.getBlueprintId() + "'>" + _physicalCard.getBlueprint().getName() + "</div> is used";
    }
}
