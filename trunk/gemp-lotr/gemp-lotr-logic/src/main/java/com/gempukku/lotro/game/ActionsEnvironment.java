package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;
import java.util.Map;

public interface ActionsEnvironment {
    public List<Action> getRequiredBeforeTriggers(Effect effect);

    public Map<String, List<Action>> getOptionalBeforeTriggers(List<String> players, Effect effect);

    public List<Action> getOptionalBeforeActions(String playerId, Effect effect);

    public List<Action> getRequiredAfterTriggers(EffectResult effectResult);

    public Map<String, List<Action>> getOptionalAfterTriggers(List<String> players, EffectResult effectResult);

    public List<Action> getOptionalAfterActions(String playerId, EffectResult effectResult);

    public void addUntilStartOfPhaseActionProxy(ActionProxy actionProxy, Phase phase);

    public void addUntilEndOfPhaseActionProxy(ActionProxy actionProxy, Phase phase);

    public void addActionToStack(Action action);
}
