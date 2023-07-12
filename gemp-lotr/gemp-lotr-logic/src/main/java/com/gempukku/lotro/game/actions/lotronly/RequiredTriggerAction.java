package com.gempukku.lotro.game.actions.lotronly;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.AbstractCostToEffectAction;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.effects.Effect;

public class RequiredTriggerAction extends AbstractCostToEffectAction {
    private final PhysicalCard _physicalCard;

    private boolean _sentMessage;
    private String _message;

    public RequiredTriggerAction(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
        if (physicalCard != null) {
            setText("Required trigger from " + GameUtils.getCardLink(_physicalCard));
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

    public void setMessage(String message) {
        _message = message;
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

            Effect effect = getNextEffect();
            if (effect != null)
                return effect;
        }
        return null;
    }
}
