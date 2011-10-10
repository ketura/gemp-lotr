package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ActivateCardEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CancelActivatedEffect extends UnrespondableEffect {
    private String _playerId;
    private ActivateCardEffect _effect;

    public CancelActivatedEffect(String playerId, ActivateCardEffect effect) {
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
