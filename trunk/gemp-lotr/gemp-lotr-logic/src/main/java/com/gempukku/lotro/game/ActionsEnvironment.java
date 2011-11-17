package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.List;

public interface ActionsEnvironment {
    public List<Action> getRequiredBeforeTriggers(Effect effect);

    public List<Action> getOptionalBeforeTriggers(String playerId, Effect effect);

    public List<Action> getOptionalBeforeActions(String playerId, Effect effect);

    public List<Action> getRequiredAfterTriggers(Collection<? extends EffectResult> effectResults);

    public List<Action> getOptionalAfterTriggers(String playerId, Collection<? extends EffectResult> effectResults);

    public List<Action> getOptionalAfterActions(String playerId, Collection<? extends EffectResult> effectResults);

    public List<Action> getPhaseActions(String playerId);

    public void addUntilStartOfPhaseActionProxy(ActionProxy actionProxy, Phase phase);

    public void addUntilEndOfPhaseActionProxy(ActionProxy actionProxy, Phase phase);

    public void addUntilEndOfTurnActionProxy(ActionProxy actionProxy);

    public void addActionToStack(Action action);

    public <T extends Action> T findTopmostActionOfType(Class<T> clazz);

    public List<PhysicalCard> getPlayedCardsInCurrentPhase();
}
