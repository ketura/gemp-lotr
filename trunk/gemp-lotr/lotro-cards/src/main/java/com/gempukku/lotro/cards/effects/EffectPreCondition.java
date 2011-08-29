package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;

public interface EffectPreCondition {
    public boolean getResult(LotroGame game);
}
