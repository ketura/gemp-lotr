package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.PlayEventEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CancelEventEffect extends UnrespondableEffect {
    private String _playerId;
    private PlayEventEffect _effect;

    public CancelEventEffect(String playerId, PlayEventEffect effect) {
        _playerId = playerId;
        _effect = effect;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        if (!_effect.isCancelled()) {
            game.getGameState().sendMessage(_playerId + " cancels effect - " + _effect.getText(game));
            _effect.cancel();
        }
    }
}
