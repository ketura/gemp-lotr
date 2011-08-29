package com.gempukku.lotro.game.state.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.ActionProxy;
import com.gempukku.lotro.game.ActionsEnvironment;
import com.gempukku.lotro.game.CompletePhysicalCardVisitor;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.ActionStack;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.*;

public class DefaultActionsEnvironment implements ActionsEnvironment {
    private LotroGame _lotroGame;
    private Set<String> _participants;
    private ActionStack _actionStack;
    private List<ActionProxy> _actionProxies = new LinkedList<ActionProxy>();
    private Map<Phase, List<ActionProxy>> _untilStartOfPhaseActionProxies = new HashMap<Phase, List<ActionProxy>>();
    private Map<Phase, List<ActionProxy>> _untilEndOfPhaseActionProxies = new HashMap<Phase, List<ActionProxy>>();

    public DefaultActionsEnvironment(LotroGame lotroGame, Set<String> participants, ActionStack actionStack) {
        _lotroGame = lotroGame;
        _participants = participants;
        _actionStack = actionStack;
    }

    public void addAlwaysOnActionProxy(ActionProxy actionProxy) {
        _actionProxies.add(actionProxy);
    }

    public void removeStartOfPhaseActionProxies(Phase phase) {
        List<ActionProxy> list = _untilStartOfPhaseActionProxies.get(phase);
        if (list != null) {
            _actionProxies.removeAll(list);
            list.clear();
        }
    }

    public void removeEndOfPhaseActionProxies(Phase phase) {
        List<ActionProxy> list = _untilEndOfPhaseActionProxies.get(phase);
        if (list != null) {
            _actionProxies.removeAll(list);
            list.clear();
        }
    }

    @Override
    public void addUntilStartOfPhaseActionProxy(ActionProxy actionProxy, Phase phase) {
        _actionProxies.add(actionProxy);
        List<ActionProxy> list = _untilStartOfPhaseActionProxies.get(phase);
        if (list == null) {
            list = new LinkedList<ActionProxy>();
            _untilStartOfPhaseActionProxies.put(phase, list);
        }
        list.add(actionProxy);
    }

    @Override
    public void addUntilEndOfPhaseActionProxy(ActionProxy actionProxy, Phase phase) {
        _actionProxies.add(actionProxy);
        List<ActionProxy> list = _untilEndOfPhaseActionProxies.get(phase);
        if (list == null) {
            list = new LinkedList<ActionProxy>();
            _untilEndOfPhaseActionProxies.put(phase, list);
        }
        list.add(actionProxy);
    }

    @Override
    public List<Action> getRequiredIsAboutToResponses(Effect effect, EffectResult effectResult) {
        GatherRequiredIsAboutToActionsVisitor gatherActions = new GatherRequiredIsAboutToActionsVisitor(effect, effectResult);

        _lotroGame.getGameState().iterateActiveCards(gatherActions);

        List<Action> gatheredActions = gatherActions.getActions();

        for (ActionProxy actionProxy : _actionProxies) {
            List<Action> actions = actionProxy.getRequiredIsAboutToActions(_lotroGame, effect, effectResult);
            if (actions != null)
                gatheredActions.addAll(actions);
        }

        return gatheredActions;
    }

    @Override
    public boolean hasOptionalIsAboutToResponse(Effect effect, EffectResult effectResult) {
        for (String participant : _participants) {
            List<Action> actions = getOptionalIsAboutToResponses(participant, effect, effectResult);
            if (actions.size() > 0)
                return true;
        }
        return false;
    }

    @Override
    public List<Action> getOptionalIsAboutToResponses(String playerId, Effect effect, EffectResult effectResult) {
        GatherIsAboutToActionsVisitor gatherActions = new GatherIsAboutToActionsVisitor(playerId, effect, effectResult);

        _lotroGame.getGameState().iterateActivableCards(playerId, gatherActions);

        List<Action> gatheredActions = gatherActions.getActions();

        for (ActionProxy actionProxy : _actionProxies) {
            List<Action> actions = actionProxy.getPlayableIsAboutToActions(playerId, _lotroGame, effect, effectResult);
            if (actions != null)
                gatheredActions.addAll(actions);
        }

        return gatheredActions;
    }

    @Override
    public List<Action> getRequiredWhenResponses(EffectResult effectResult) {
        GatherRequiredWhenActionsVisitor gatherActions = new GatherRequiredWhenActionsVisitor(effectResult);

        _lotroGame.getGameState().iterateActiveCards(gatherActions);

        List<Action> gatheredActions = gatherActions.getActions();

        for (ActionProxy actionProxy : _actionProxies) {
            List<Action> actions = actionProxy.getRequiredWhenActions(_lotroGame, effectResult);
            if (actions != null)
                gatheredActions.addAll(actions);
        }

        return gatheredActions;
    }

    @Override
    public boolean hasOptionalWhenResponse(EffectResult effectResult) {
        for (String playerId : _participants) {
            List<Action> actions = getOptionalWhenResponses(playerId, effectResult);
            if (actions.size() > 0)
                return true;
        }
        return false;
    }

    @Override
    public List<Action> getOptionalWhenResponses(String playerId, EffectResult effectResult) {
        GatherWhenActionsVisitor gatherActions = new GatherWhenActionsVisitor(playerId, effectResult);

        _lotroGame.getGameState().iterateActivableCards(playerId, gatherActions);

        List<Action> gatheredActions = gatherActions.getActions();

        for (ActionProxy actionProxy : _actionProxies) {
            List<Action> actions = actionProxy.getPlayableWhenActions(playerId, _lotroGame, effectResult);
            if (actions != null)
                gatheredActions.addAll(actions);
        }

        return gatheredActions;
    }

    @Override
    public void addActionToStack(Action action) {
        _actionStack.stackAction(action);
    }

    private class GatherRequiredWhenActionsVisitor extends CompletePhysicalCardVisitor {
        private EffectResult _effectResult;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherRequiredWhenActionsVisitor(EffectResult effectResult) {
            _effectResult = effectResult;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> actions = physicalCard.getBlueprint().getRequiredWhenActions(_lotroGame, _effectResult, physicalCard);
            if (actions != null)
                _actions.addAll(actions);
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherRequiredIsAboutToActionsVisitor extends CompletePhysicalCardVisitor {
        private Effect _effect;
        private EffectResult _effectResult;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherRequiredIsAboutToActionsVisitor(Effect effect, EffectResult effectResult) {
            _effect = effect;
            _effectResult = effectResult;
        }

        @Override
        public void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> actions = physicalCard.getBlueprint().getRequiredIsAboutToActions(_lotroGame, _effect, _effectResult, physicalCard);
            if (actions != null)
                _actions.addAll(actions);
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherIsAboutToActionsVisitor extends CompletePhysicalCardVisitor {
        private String _playerId;
        private Effect _effect;
        private EffectResult _effectResult;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherIsAboutToActionsVisitor(String playerId, Effect effect, EffectResult effectResult) {
            _playerId = playerId;
            _effect = effect;
            _effectResult = effectResult;
        }

        @Override
        public void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> actions = physicalCard.getBlueprint().getPlayableIsAboutToActions(_playerId, _lotroGame, _effect, _effectResult, physicalCard);
            if (actions != null)
                _actions.addAll(actions);
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherWhenActionsVisitor extends CompletePhysicalCardVisitor {
        private String _playerId;
        private EffectResult _effectResult;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherWhenActionsVisitor(String playerId, EffectResult effectResult) {
            _playerId = playerId;
            _effectResult = effectResult;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> actions = physicalCard.getBlueprint().getPlayableWhenActions(_playerId, _lotroGame, _effectResult, physicalCard);
            if (actions != null)
                _actions.addAll(actions);
        }

        public List<Action> getActions() {
            return _actions;
        }
    }
}
