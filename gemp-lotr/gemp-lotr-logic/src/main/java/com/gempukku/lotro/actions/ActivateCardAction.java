package com.gempukku.lotro.actions;

import com.gempukku.lotro.actions.lotronly.AbstractCostToEffectAction;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.ActivateCardEffect;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.Effect;

public class ActivateCardAction extends AbstractCostToEffectAction {

    protected final LotroPhysicalCard _physicalCard;
    protected ActivateCardEffect _activateCardEffect;
    protected boolean _sentMessage;
    protected boolean _activated;
    protected boolean _prevented;

    public ActivateCardAction(LotroPhysicalCard physicalCard) {
        _physicalCard = physicalCard;
        setText("Use " + GameUtils.getFullName(_physicalCard));
    }

    @Override
    public Type getType() {
        return Type.SPECIAL_ABILITY;
    }

    @Override
    public LotroPhysicalCard getActionSource() {
        return _physicalCard;
    }

    @Override
    public LotroPhysicalCard getActionAttachedToCard() {
        return _physicalCard;
    }

    public void prevent() {
        _prevented = true;
    }

    @Override
    public Effect nextEffect(DefaultGame game) {
        if (!_sentMessage) {
            _sentMessage = true;
            if (_physicalCard != null && _physicalCard.getZone().isInPlay())
                game.getGameState().activatedCard(getPerformingPlayer(), _physicalCard);
            game.getGameState().sendMessage(GameUtils.getCardLink(_physicalCard) + " is used");
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

            if (!_activateCardEffect.getActivateCardResult().isEffectCancelled() && !_prevented)
                return getNextEffect();
        }
        return null;
    }
}
