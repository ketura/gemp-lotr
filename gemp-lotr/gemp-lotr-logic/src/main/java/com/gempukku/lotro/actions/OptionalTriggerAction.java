package com.gempukku.lotro.actions;

import com.gempukku.lotro.actions.lotronly.AbstractCostToEffectAction;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.Effect;

public class OptionalTriggerAction extends AbstractCostToEffectAction {
    private LotroPhysicalCard _physicalCard;
    private final LotroPhysicalCard _actionAttachedToCard;

    private String _message;

    private boolean _sentMessage;
    private String _triggerIdentifier;

    public OptionalTriggerAction(String triggerIdentifier, LotroPhysicalCard attachedToCard) {
        _actionAttachedToCard = attachedToCard;
        _triggerIdentifier = triggerIdentifier;
    }

    public OptionalTriggerAction(LotroPhysicalCard physicalCard) {
        _physicalCard = physicalCard;
        _actionAttachedToCard = physicalCard;

        setText("Optional trigger from " + GameUtils.getCardLink(_physicalCard));
        _message = GameUtils.getCardLink(_physicalCard) + " optional triggered effect is used";
        _triggerIdentifier = String.valueOf(physicalCard.getCardId());
    }

    public void setTriggerIdentifier(String triggerIdentifier) {
        _triggerIdentifier = triggerIdentifier;
    }

    public String getTriggerIdentifier() {
        return _triggerIdentifier;
    }

    public void setMessage(String message) {
        _message = message;
    }

    @Override
    public Type getType() {
        return Type.TRIGGER;
    }

    @Override
    public LotroPhysicalCard getActionSource() {
        return _physicalCard;
    }

    @Override
    public LotroPhysicalCard getActionAttachedToCard() {
        return _actionAttachedToCard;
    }

    @Override
    public Effect nextEffect(DefaultGame game) {
        if (!_sentMessage) {
            _sentMessage = true;
            if (_physicalCard != null)
                game.getGameState().activatedCard(getPerformingPlayer(), _physicalCard);
            if (_message != null)
                game.getGameState().sendMessage(_message);
        }

        if (!isCostFailed()) {
            Effect cost = getNextCost();
            if (cost != null)
                return cost;

            return getNextEffect();
        }
        return null;
    }
}
