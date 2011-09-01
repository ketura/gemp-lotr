package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;
import java.util.Map;

public interface ActionsEnvironment {
    public List<Action> getRequiredIsAboutToResponses(Effect effect, EffectResult effectResult);

    public List<Action> getOptionalIsAboutToResponses(String playerId, Effect effect, EffectResult effectResult);

    public List<Action> getRequiredOneTimeResponses(EffectResult effectResult);

    public Map<String, List<Action>> getOptionalOneTimeResponses(List<String> players, EffectResult effectResult);

    public List<Action> getOptionalResponses(String playerId, EffectResult effectResult);

    public void addUntilStartOfPhaseActionProxy(ActionProxy actionProxy, Phase phase);

    public void addUntilEndOfPhaseActionProxy(ActionProxy actionProxy, Phase phase);

    public void addActionToStack(Action action);
}
