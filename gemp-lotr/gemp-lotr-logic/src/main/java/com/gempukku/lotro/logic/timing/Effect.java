package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public interface Effect {
    public EffectResult.Type getType();

    public String getText(LotroGame game);

    public void setFailed();

    public boolean isFailed();

    public void cancel();

    public boolean isCancelled();

    public boolean canPlayEffect(LotroGame game);

    public EffectResult[] playEffect(LotroGame game);
}
