package com.gempukku.lotro.game;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public interface ActionProxy {
    public List<? extends Action> getRequiredIsAboutToActions(LotroGame game, Effect effect, EffectResult effectResult);

    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult);

    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game);

    public List<? extends Action> getPlayableIsAboutToActions(String playerId, LotroGame game, Effect effect, EffectResult effectResult);

    public List<? extends Action> getPlayableWhenActions(String playerId, LotroGame game, EffectResult effectResult);
}
