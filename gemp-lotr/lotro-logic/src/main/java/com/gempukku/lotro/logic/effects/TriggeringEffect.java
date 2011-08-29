package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class TriggeringEffect extends AbstractEffect {
    private EffectResult _effectResult;

    public TriggeringEffect(EffectResult effectResult) {
        _effectResult = effectResult;
    }

    @Override
    public EffectResult getRespondableResult() {
        return _effectResult;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return true;
    }

    @Override
    public void playEffect(LotroGame game) {
    }
}
