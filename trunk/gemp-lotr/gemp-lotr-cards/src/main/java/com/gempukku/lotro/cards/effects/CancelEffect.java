package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CancelEffect extends UnrespondableEffect {
    private String _playerId;
    private Effect _effect;

    public CancelEffect(String playerId, Effect effect) {
        _playerId = playerId;
        _effect = effect;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return (!_effect.isCancelled() && !_effect.isFailed());
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getGameState().sendMessage(_playerId + " cancels effect - " + _effect.getText());
        _effect.cancel();
    }
}
