package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class TransferPermanentEffect extends AbstractEffect {
    private PhysicalCard _physicalCard;
    private PhysicalCard _targetCard;

    public TransferPermanentEffect(PhysicalCard physicalCard, PhysicalCard targetCard) {
        _physicalCard = physicalCard;
        _targetCard = targetCard;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _targetCard.getZone().isInPlay()
                && _physicalCard.getZone().isInPlay()
                && game.getModifiersQuerying().canHaveTransferredOn(game.getGameState(), _physicalCard, _targetCard);
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(_physicalCard.getOwner() + " transfers " + GameUtils.getCardLink(_physicalCard) + " to " + GameUtils.getCardLink(_targetCard));
            gameState.transferCard(_physicalCard, _targetCard);

            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
