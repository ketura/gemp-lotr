package com.gempukku.lotro.game;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public abstract class AbstractActionProxy implements ActionProxy {
    @Override
    public List<? extends Action> getRequiredBeforeTriggers(LotroGame game, Effect effect) {
        return null;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
        return null;
    }
}
