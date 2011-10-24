package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class RequiredTriggerAction extends AbstractCostToEffectAction {
    private PhysicalCard _physicalCard;

    private boolean _sentMessage;

    private String _text;

    public RequiredTriggerAction(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
        if (physicalCard != null)
            _text = "Required trigger from " + GameUtils.getCardLink(_physicalCard);
    }

    @Override
    public PhysicalCard getActionSource() {
        return _physicalCard;
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return _physicalCard;
    }

    public void setText(String text) {
        _text = text;
    }

    @Override
    public String getText(LotroGame game) {
        return _text;
    }

    @Override
    public Effect nextEffect(LotroGame game) {
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
        return GameUtils.getCardLink(_physicalCard) + " required triggered effect is used";
    }
}
