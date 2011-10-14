package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class AttachCardEffect extends AbstractEffect {
    private PhysicalCard _physicalCard;
    private PhysicalCard _targetCard;

    public AttachCardEffect(PhysicalCard physicalCard, PhysicalCard targetCard) {
        _physicalCard = physicalCard;
        _targetCard = targetCard;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _targetCard.getZone().isInPlay()
                && game.getModifiersQuerying().canHavePlayedOn(game.getGameState(), _physicalCard, _targetCard);
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            gameState.attachCard(_physicalCard, _targetCard);
            gameState.startAffecting(game, _physicalCard, game.getModifiersEnvironment());
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
