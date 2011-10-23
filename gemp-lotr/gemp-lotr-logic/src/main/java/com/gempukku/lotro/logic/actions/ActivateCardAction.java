package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.ActivateCardEffect;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class ActivateCardAction extends AbstractCostToEffectAction {
    private PhysicalCard _physicalCard;

    private ActivateCardEffect _activateCardEffect;

    private boolean _sentMessage;
    private boolean _activated;

    private String _text;

    public ActivateCardAction(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
        _text = "Use " + _physicalCard.getBlueprint().getName();
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

            if (!_activated) {
                _activated = true;
                _activateCardEffect = new ActivateCardEffect(_physicalCard, getActionTimeword());
                return _activateCardEffect;
            }

            if (!_activateCardEffect.getActivateCardResult().isEffectCancelled()) {
                Effect effect = getNextEffect();
                if (effect != null)
                    return effect;
            }
        }
        return null;
    }

    private String getMessage() {
        return GameUtils.getCardLink(_physicalCard) + " is used";
    }
}
