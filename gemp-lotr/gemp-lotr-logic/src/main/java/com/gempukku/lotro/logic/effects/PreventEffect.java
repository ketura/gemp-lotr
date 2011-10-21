package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.Preventable;

public class PreventEffect extends AbstractEffect {
    private Preventable _preventable;

    public PreventEffect(Preventable preventable) {
        _preventable = preventable;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            _preventable.prevent();
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return !_preventable.isPrevented();
    }
}
