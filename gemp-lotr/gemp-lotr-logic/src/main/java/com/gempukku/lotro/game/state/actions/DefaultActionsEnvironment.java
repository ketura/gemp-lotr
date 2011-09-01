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
    public List<Action> getRequiredBeforeTriggers(Effect effect, EffectResult effectResult) {
        GatherRequiredBeforeTriggers gatherActions = new GatherRequiredBeforeTriggers(effect, effectResult);

        _lotroGame.getGameState().iterateActiveCards(gatherActions);

        List<Action> gatheredActions = gatherActions.getActions();

        for (ActionProxy actionProxy : _actionProxies) {
            List<? extends Action> actions = actionProxy.getRequiredBeforeTriggers(_lotroGame, effect, effectResult);
            if (actions != null)
                gatheredActions.addAll(actions);
        }

        return gatheredActions;
    }

    @Override
    public Map<String, List<Action>> getOptionalBeforeTriggers(List<String> players, Effect effect, EffectResult effectResult) {
        Map<String, List<Action>> result = new HashMap<String, List<Action>>();
        for (String playerId : players) {
            GatherOptionalBeforeTriggers gatherActions = new GatherOptionalBeforeTriggers(playerId, effect, effectResult);

            _lotroGame.getGameState().iterateActivableCards(playerId, gatherActions);

            List<Action> gatheredActions = gatherActions.getActions();

            result.put(playerId, gatheredActions);
        }

        return result;
    }

    @Override
    public List<Action> getOptionalBeforeActions(String playerId, Effect effect, EffectResult effectResult) {
        GatherOptionalBeforeActions gatherActions = new GatherOptionalBeforeActions(playerId, effect, effectResult);

        _lotroGame.getGameState().iterateActivableCards(playerId, gatherActions);

        return gatherActions.getActions();
    }

    @Override
    public List<Action> getRequiredAfterTriggers(EffectResult effectResult) {
        GatherRequiredAfterTriggers gatherActions = new GatherRequiredAfterTriggers(effectResult);

        _lotroGame.getGameState().iterateActiveCards(gatherActions);

        List<Action> gatheredActions = gatherActions.getActions();

        for (ActionProxy actionProxy : _actionProxies) {
            List<? extends Action> actions = actionProxy.getRequiredAfterTriggers(_lotroGame, effectResult);
            if (actions != null)
                gatheredActions.addAll(actions);
        }

        return gatheredActions;
    }

    @Override
    public Map<String, List<Action>> getOptionalAfterTriggers(List<String> players, EffectResult effectResult) {
        Map<String, List<Action>> result = new HashMap<String, List<Action>>();
        for (String playerId : players) {
            GatherOptionalAfterTriggers gatherActions = new GatherOptionalAfterTriggers(playerId, effectResult);

            _lotroGame.getGameState().iterateActivableCards(playerId, gatherActions);

            List<Action> gatheredActions = gatherActions.getActions();

            result.put(playerId, gatheredActions);
        }

        return result;
    }

    @Override
    public List<Action> getOptionalAfterActions(String playerId, EffectResult effectResult) {
        GatherOptionalAfterActions gatherAfterActions = new GatherOptionalAfterActions(playerId, effectResult);

        _lotroGame.getGameState().iterateActivableCards(playerId, gatherAfterActions);

        return gatherAfterActions.getActions();
    }

    @Override
    public void addActionToStack(Action action) {
        _actionStack.stackAction(action);
    }

    private class GatherRequiredAfterTriggers extends CompletePhysicalCardVisitor {
        private EffectResult _effectResult;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherRequiredAfterTriggers(EffectResult effectResult) {
            _effectResult = effectResult;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> actions = physicalCard.getBlueprint().getRequiredAfterTriggers(_lotroGame, _effectResult, physicalCard);
            if (actions != null)
                _actions.addAll(actions);
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherRequiredBeforeTriggers extends CompletePhysicalCardVisitor {
        private Effect _effect;
        private EffectResult _effectResult;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherRequiredBeforeTriggers(Effect effect, EffectResult effectResult) {
            _effect = effect;
            _effectResult = effectResult;
        }

        @Override
        public void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> actions = physicalCard.getBlueprint().getRequiredBeforeTriggers(_lotroGame, _effect, _effectResult, physicalCard);
            if (actions != null)
                _actions.addAll(actions);
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherOptionalBeforeActions extends CompletePhysicalCardVisitor {
        private String _playerId;
        private Effect _effect;
        private EffectResult _effectResult;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherOptionalBeforeActions(String playerId, Effect effect, EffectResult effectResult) {
            _playerId = playerId;
            _effect = effect;
            _effectResult = effectResult;
        }

        @Override
        public void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> actions = physicalCard.getBlueprint().getOptionalBeforeActions(_playerId, _lotroGame, _effect, _effectResult, physicalCard);
            if (actions != null)
                _actions.addAll(actions);
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherOptionalAfterTriggers extends CompletePhysicalCardVisitor {
        private String _playerId;
        private EffectResult _effectResult;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherOptionalAfterTriggers(String playerId, EffectResult effectResult) {
            _playerId = playerId;
            _effectResult = effectResult;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> actions = physicalCard.getBlueprint().getOptionalAfterTriggers(_playerId, _lotroGame, _effectResult, physicalCard);
            if (actions != null)
                _actions.addAll(actions);
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherOptionalBeforeTriggers extends CompletePhysicalCardVisitor {
        private String _playerId;
        private Effect _effect;
        private EffectResult _effectResult;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherOptionalBeforeTriggers(String playerId, Effect effect, EffectResult effectResult) {
            _playerId = playerId;
            _effect = effect;
            _effectResult = effectResult;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> actions = physicalCard.getBlueprint().getOptionalBeforeTriggers(_playerId, _lotroGame, _effect, _effectResult, physicalCard);
            if (actions != null)
                _actions.addAll(actions);
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherOptionalAfterActions extends CompletePhysicalCardVisitor {
        private String _playerId;
        private EffectResult _effectResult;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherOptionalAfterActions(String playerId, EffectResult effectResult) {
            _playerId = playerId;
            _effectResult = effectResult;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> actions = physicalCard.getBlueprint().getOptionalAfterActions(_playerId, _lotroGame, _effectResult, physicalCard);
            if (actions != null)
                _actions.addAll(actions);
        }

        public List<Action> getActions() {
            return _actions;
        }
    }
}
