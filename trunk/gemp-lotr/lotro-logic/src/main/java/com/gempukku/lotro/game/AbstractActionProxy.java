package com.gempukku.lotro.game;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public abstract class AbstractActionProxy implements ActionProxy {
    @Override
    public List<Action> getRequiredIsAboutToActions(LotroGame lotroGame, Effect effect, EffectResult effectResult) {
        return null;
    }

    @Override
    public List<Action> getRequiredWhenActions(LotroGame lotroGame, EffectResult effectResult) {
        return null;
    }

    @Override
    public List<Action> getPlayablePhaseActions(String playerId, LotroGame lotroGame) {
        return null;
    }

    @Override
    public List<Action> getPlayableIsAboutToActions(String playerId, LotroGame lotroGame, Effect effect, EffectResult effectResult) {
        return null;
    }

    @Override
    public List<Action> getPlayableWhenActions(String playerId, LotroGame lotroGame, EffectResult effectResult) {
        return null;
    }
}
