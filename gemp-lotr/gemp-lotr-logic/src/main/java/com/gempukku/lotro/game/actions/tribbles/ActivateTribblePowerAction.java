package com.gempukku.lotro.game.actions.tribbles;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.ActivateCardAction;
import com.gempukku.lotro.game.effects.ActivateCardEffect;
import com.gempukku.lotro.game.effects.Effect;
import com.gempukku.lotro.game.rules.GameUtils;

public class ActivateTribblePowerAction extends ActivateCardAction {
    public ActivateTribblePowerAction(LotroPhysicalCard physicalCard) {
        super(physicalCard);
        setText("Activate " + GameUtils.getFullName(_physicalCard));
    }

    @Override
    public Type getType() {
        return Type.TRIBBLE_POWER;
    }

    @Override
    public Effect nextEffect(DefaultGame game) {
        if (!_sentMessage) {
            _sentMessage = true;
            if (_physicalCard != null && _physicalCard.getZone().isInPlay())
                game.getGameState().activatedCard(getPerformingPlayer(), _physicalCard);
            game.getGameState().sendMessage(GameUtils.getCardLink(_physicalCard) + " is activated");
        }

        if (!_activated) {
            _activated = true;
            _activateCardEffect = new ActivateCardEffect(_physicalCard, getActionTimeword());
            return _activateCardEffect;
        }

        if (!_activateCardEffect.getActivateCardResult().isEffectCancelled() && !_prevented)
            return getNextEffect();

        return null;
    }
}
