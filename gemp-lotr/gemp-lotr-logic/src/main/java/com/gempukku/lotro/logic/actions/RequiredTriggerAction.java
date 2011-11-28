package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.Effect;

public class RequiredTriggerAction extends AbstractCostToEffectAction {
    private PhysicalCard _physicalCard;

    private boolean _sentMessage;
    private String _message;

    private String _text;

    public RequiredTriggerAction(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
        if (physicalCard != null) {
            _text = "Required trigger from " + GameUtils.getCardLink(_physicalCard);
            _message = GameUtils.getCardLink(_physicalCard) + " required triggered effect is used";
        }
    }

    @Override
    public Type getType() {
        return Type.TRIGGER;
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

    public void setMessage(String message) {
        _message = message;
    }

    @Override
    public String getText(LotroGame game) {
        return _text;
    }

    @Override
    public Effect nextEffect(LotroGame game) {
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

            Effect effect = getNextEffect();
            if (effect != null)
                return effect;
        }
        return null;
    }
}
