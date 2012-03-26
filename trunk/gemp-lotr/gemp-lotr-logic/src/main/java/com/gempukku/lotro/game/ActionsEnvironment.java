package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ActionsEnvironment {
    public List<Action> getRequiredBeforeTriggers(Effect effect);

    public List<Action> getOptionalBeforeTriggers(String playerId, Effect effect);

    public List<Action> getOptionalBeforeActions(String playerId, Effect effect);

    public List<Action> getRequiredAfterTriggers(Collection<? extends EffectResult> effectResults);

    public Map<OptionalTriggerAction, EffectResult> getOptionalAfterTriggers(String playerId, Collection<? extends EffectResult> effectResults);

    public List<Action> getOptionalAfterActions(String playerId, Collection<? extends EffectResult> effectResults);

    public List<Action> getPhaseActions(String playerId);

    public void addUntilStartOfPhaseActionProxy(ActionProxy actionProxy, Phase phase);

    public void addUntilEndOfPhaseActionProxy(ActionProxy actionProxy, Phase phase);

    public void addUntilEndOfTurnActionProxy(ActionProxy actionProxy);

    public void addActionToStack(Action action);

    public void emitEffectResult(EffectResult effectResult);

    public <T extends Action> T findTopmostActionOfType(Class<T> clazz);

    public List<PhysicalCard> getPlayedCardsInCurrentPhase();

    public boolean hasWonSkirmishThisTurn(LotroGame game, Filterable... filters);

    public boolean hasLostSkirmishThisTurn(LotroGame game, Filterable... filters);

    public boolean wasAssignedThisTurn(LotroGame game, Filterable... filters);
}
