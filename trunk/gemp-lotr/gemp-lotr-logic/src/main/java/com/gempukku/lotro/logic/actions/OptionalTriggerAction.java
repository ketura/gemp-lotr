package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class OptionalTriggerAction extends AbstractCostToEffectAction {
    private PhysicalCard _physicalCard;

    private boolean _sentMessage;

    public OptionalTriggerAction(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public PhysicalCard getActionSource() {
        return _physicalCard;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
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

    private String getMessage() {
        return GameUtils.getCardLink(_physicalCard) + " optional triggered effect is used";
    }
}
