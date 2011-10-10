package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.ActivateCardEffect;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class ActivateCardAction extends AbstractCostToEffectAction {
    private PhysicalCard _physicalCard;
    private Keyword _type;

    private ActivateCardEffect _activateCardEffect;

    private boolean _sentMessage;
    private boolean _activated;

    public ActivateCardAction(PhysicalCard physicalCard, Keyword type) {
        _physicalCard = physicalCard;
        _type = type;

        _activateCardEffect = new ActivateCardEffect(physicalCard);
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

            if (!_activated) {
                _activated = true;
                return _activateCardEffect;
            }

            if (!_activateCardEffect.isCancelled()) {
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
