package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public interface ActionsEnvironment {
    public List<Action> getRequiredIsAboutToResponses(Effect effect, EffectResult effectResult);

    public boolean hasOptionalIsAboutToResponse(Effect effect, EffectResult effectResult);

    public List<Action> getOptionalIsAboutToResponses(String playerId, Effect effect, EffectResult effectResult);

    public List<Action> getRequiredWhenResponses(EffectResult effectResult);

    public boolean hasOptionalWhenResponse(EffectResult effectResult);

    public List<Action> getOptionalWhenResponses(String playerId, EffectResult effectResult);

    public void addUntilStartOfPhaseActionProxy(ActionProxy actionProxy, Phase phase);

    public void addUntilEndOfPhaseActionProxy(ActionProxy actionProxy, Phase phase);

    public void addActionToStack(Action action);
}
