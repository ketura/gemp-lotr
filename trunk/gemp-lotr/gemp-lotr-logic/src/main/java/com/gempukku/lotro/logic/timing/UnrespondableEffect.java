package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public abstract class UnrespondableEffect implements Effect {
    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    protected abstract void doPlayEffect(LotroGame game);

    @Override
    public final EffectResult[] playEffect(LotroGame game) {
        doPlayEffect(game);
        return null;
    }
}
