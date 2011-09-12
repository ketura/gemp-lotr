package com.gempukku.lotro.game;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public interface ActionProxy {
    public List<? extends Action> getRequiredBeforeTriggers(LotroGame lotroGame, Effect effect);

    public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult);
}
