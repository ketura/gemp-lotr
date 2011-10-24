package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class OptionalTriggerAction extends AbstractCostToEffectAction {
    private PhysicalCard _physicalCard;
    private PhysicalCard _actionAttachedToCard;

    private boolean _sentMessage;
    private String _text;

    public OptionalTriggerAction(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
        _actionAttachedToCard = physicalCard;

        _text = "Optional trigger from " + GameUtils.getCardLink(_physicalCard);
    }

    public void setActionAttachedToCard(PhysicalCard actionAttachedToCard) {
        _actionAttachedToCard = actionAttachedToCard;
    }

    @Override
    public PhysicalCard getActionSource() {
        return _physicalCard;
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return _actionAttachedToCard;
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
            game.getGameState().activatedCard(_physicalCard.getOwner(), _physicalCard);
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
