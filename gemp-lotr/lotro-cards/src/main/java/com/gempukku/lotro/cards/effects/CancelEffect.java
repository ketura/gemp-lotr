package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CancelEffect extends UnrespondableEffect {
    private Effect _effect;

    public CancelEffect(Effect effect) {
        _effect = effect;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return (!_effect.isCancelled() && !_effect.isFailed());
    }

    @Override
    public void playEffect(LotroGame game) {
        _effect.cancel();
    }
}
