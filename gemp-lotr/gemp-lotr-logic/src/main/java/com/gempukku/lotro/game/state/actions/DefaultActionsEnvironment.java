package com.gempukku.lotro.game.state.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.ActionProxy;
import com.gempukku.lotro.game.ActionsEnvironment;
import com.gempukku.lotro.game.CompletePhysicalCardVisitor;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.ActionStack;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.processes.GatherPlayableActionsFromDiscardVisitor;
import com.gempukku.lotro.logic.timing.processes.GatherPlayableActionsFromStackedVisitor;
import com.gempukku.lotro.logic.timing.rules.CharacterDeathRule;

import java.util.*;

public class DefaultActionsEnvironment implements ActionsEnvironment {
    private LotroGame _lotroGame;
    private ActionStack _actionStack;
    private List<ActionProxy> _actionProxies = new LinkedList<ActionProxy>();
    private Map<Phase, List<ActionProxy>> _untilStartOfPhaseActionProxies = new HashMap<Phase, List<ActionProxy>>();
    private Map<Phase, List<ActionProxy>> _untilEndOfPhaseActionProxies = new HashMap<Phase, List<ActionProxy>>();
    private List<ActionProxy> _untilEndOfTurnActionProxies = new LinkedList<ActionProxy>();

    public DefaultActionsEnvironment(LotroGame lotroGame, ActionStack actionStack) {
        _lotroGame = lotroGame;
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

    public void removeEndOfTurnActionProxies() {
        _actionProxies.removeAll(_untilEndOfTurnActionProxies);
        _untilEndOfTurnActionProxies.clear();
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
    public void addUntilEndOfTurnActionProxy(ActionProxy actionProxy) {
        _actionProxies.add(actionProxy);
        _untilEndOfTurnActionProxies.add(actionProxy);
    }

    @Override
    public List<Action> getRequiredBeforeTriggers(Effect effect) {
        GatherRequiredBeforeTriggers gatherActions = new GatherRequiredBeforeTriggers(effect);

        _lotroGame.getGameState().iterateActiveTextCards(gatherActions);

        List<Action> gatheredActions = gatherActions.getActions();

        for (ActionProxy actionProxy : _actionProxies) {
            List<? extends Action> actions = actionProxy.getRequiredBeforeTriggers(_lotroGame, effect);
            if (actions != null)
                gatheredActions.addAll(actions);
        }

        return gatheredActions;
    }

    @Override
    public List<Action> getOptionalBeforeTriggers(String playerId, Effect effect) {
        GatherOptionalBeforeTriggers gatherActions = new GatherOptionalBeforeTriggers(playerId, effect);

        _lotroGame.getGameState().iterateActiveTextCards(playerId, gatherActions);

        List<Action> actionList = gatherActions.getActions();
        for (Action action : actionList)
            action.setPerformingPlayer(playerId);

        return actionList;
    }

    @Override
    public List<Action> getOptionalBeforeActions(String playerId, Effect effect) {
        GatherOptionalBeforeActions gatherActions = new GatherOptionalBeforeActions(playerId, effect);

        _lotroGame.getGameState().iterateActivableCards(playerId, gatherActions);

        List<Action> result = new LinkedList<Action>();

        for (Action action : gatherActions.getActions()) {
            action.setPerformingPlayer(playerId);
            if (_lotroGame.getModifiersQuerying().canPlayAction(_lotroGame.getGameState(), playerId, action))
                result.add(action);
        }

        return result;
    }

    @Override
    public List<Action> getRequiredAfterTriggers(Collection<? extends EffectResult> effectResults) {
        GatherRequiredAfterTriggers gatherActions = new GatherRequiredAfterTriggers(effectResults);

        _lotroGame.getGameState().iterateActiveTextCards(gatherActions);

        List<Action> gatheredActions = gatherActions.getActions();
        CharacterDeathRule characterDeathRule = new CharacterDeathRule();
        List<RequiredTriggerAction> killEffects = characterDeathRule.getKillEffects(_lotroGame);
        if (killEffects != null)
            gatheredActions.addAll(killEffects);

        if (effectResults != null) {
            for (ActionProxy actionProxy : _actionProxies) {
                for (EffectResult effectResult : effectResults) {
                    List<? extends Action> actions = actionProxy.getRequiredAfterTriggers(_lotroGame, effectResult);
                    if (actions != null)
                        gatheredActions.addAll(actions);
                }
            }
        }

        return gatheredActions;
    }

    @Override
    public List<Action> getOptionalAfterTriggers(String playerId, Collection<? extends EffectResult> effectResults) {
        GatherOptionalAfterTriggers gatherActions = new GatherOptionalAfterTriggers(playerId, effectResults);

        _lotroGame.getGameState().iterateActiveTextCards(playerId, gatherActions);

        final List<Action> gatheredActions = gatherActions.getActions();

        if (effectResults != null) {
            for (ActionProxy actionProxy : _actionProxies) {
                for (EffectResult effectResult : effectResults) {
                    List<? extends Action> actions = actionProxy.getOptionalAfterTriggers(playerId, _lotroGame, effectResult);
                    if (actions != null)
                        gatheredActions.addAll(actions);
                }
            }
        }

        for (Action gatheredAction : gatheredActions)
            gatheredAction.setPerformingPlayer(playerId);

        return gatheredActions;
    }

    @Override
    public List<Action> getOptionalAfterActions(String playerId, Collection<? extends EffectResult> effectResults) {
        GatherOptionalAfterActions gatherAfterActions = new GatherOptionalAfterActions(playerId, effectResults);

        _lotroGame.getGameState().iterateActivableCards(playerId, gatherAfterActions);

        List<Action> result = new LinkedList<Action>();

        for (Action action : gatherAfterActions.getActions()) {
            action.setPerformingPlayer(playerId);
            if (_lotroGame.getModifiersQuerying().canPlayAction(_lotroGame.getGameState(), playerId, action))
                result.add(action);
        }

        return result;
    }

    @Override
    public List<Action> getPhaseActions(String playerId) {
        GatherPhaseActionsVisitor visitor = new GatherPhaseActionsVisitor(_lotroGame, playerId);
        _lotroGame.getGameState().iterateActivableCards(playerId, visitor);

        GatherPlayableActionsFromStackedVisitor stackedVisitor = new GatherPlayableActionsFromStackedVisitor(_lotroGame, playerId);
        _lotroGame.getGameState().iterateStackedActivableCards(playerId, stackedVisitor);

        GatherPlayableActionsFromDiscardVisitor discardVisitor = new GatherPlayableActionsFromDiscardVisitor(_lotroGame, playerId);
        _lotroGame.getGameState().iterateDiscardActivableCards(playerId, discardVisitor);

        List<Action> playableActions = new LinkedList<Action>();

        for (Action action : visitor.getActions()) {
            action.setActionTimeword(_lotroGame.getGameState().getCurrentPhase());
            action.setPerformingPlayer(playerId);
            if (_lotroGame.getModifiersQuerying().canPlayAction(_lotroGame.getGameState(), playerId, action))
                playableActions.add(action);
        }

        for (Action action : stackedVisitor.getActions()) {
            action.setActionTimeword(_lotroGame.getGameState().getCurrentPhase());
            action.setPerformingPlayer(playerId);
            if (_lotroGame.getModifiersQuerying().canPlayAction(_lotroGame.getGameState(), playerId, action))
                playableActions.add(action);
        }

        return playableActions;
    }

    @Override
    public void addActionToStack(Action action) {
        _actionStack.stackAction(action);
    }

    @Override
    public <T extends Action> T findTopmostActionOfType(Class<T> clazz) {
        return _actionStack.findTopmostActionOfType(clazz);
    }

    private class GatherRequiredAfterTriggers extends CompletePhysicalCardVisitor {
        private Collection<? extends EffectResult> _effectResults;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherRequiredAfterTriggers(Collection<? extends EffectResult> effectResults) {
            _effectResults = effectResults;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame.getGameState(), physicalCard)) {
                if (_effectResults != null)
                    for (EffectResult effectResult : _effectResults) {
                        List<? extends Action> actions = physicalCard.getBlueprint().getRequiredAfterTriggers(_lotroGame, effectResult, physicalCard);
                        if (actions != null)
                            _actions.addAll(actions);
                    }
            }
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherRequiredBeforeTriggers extends CompletePhysicalCardVisitor {
        private Effect _effect;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherRequiredBeforeTriggers(Effect effect) {
            _effect = effect;
        }

        @Override
        public void doVisitPhysicalCard(PhysicalCard physicalCard) {
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame.getGameState(), physicalCard)) {
                List<? extends Action> actions = physicalCard.getBlueprint().getRequiredBeforeTriggers(_lotroGame, _effect, physicalCard);
                if (actions != null)
                    _actions.addAll(actions);
            }
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherOptionalBeforeActions extends CompletePhysicalCardVisitor {
        private String _playerId;
        private Effect _effect;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherOptionalBeforeActions(String playerId, Effect effect) {
            _playerId = playerId;
            _effect = effect;
        }

        @Override
        public void doVisitPhysicalCard(PhysicalCard physicalCard) {
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame.getGameState(), physicalCard)) {
                List<? extends Action> actions = physicalCard.getBlueprint().getOptionalBeforeActions(_playerId, _lotroGame, _effect, physicalCard);
                if (actions != null)
                    _actions.addAll(actions);
            }
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherOptionalAfterTriggers extends CompletePhysicalCardVisitor {
        private String _playerId;
        private Collection<? extends EffectResult> _effectResults;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherOptionalAfterTriggers(String playerId, Collection<? extends EffectResult> effectResults) {
            _playerId = playerId;
            _effectResults = effectResults;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame.getGameState(), physicalCard)) {
                if (_effectResults != null)
                    for (EffectResult effectResult : _effectResults) {
                        List<? extends Action> actions = physicalCard.getBlueprint().getOptionalAfterTriggers(_playerId, _lotroGame, effectResult, physicalCard);
                        if (actions != null)
                            _actions.addAll(actions);
                    }
            }
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherOptionalBeforeTriggers extends CompletePhysicalCardVisitor {
        private String _playerId;
        private Effect _effect;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherOptionalBeforeTriggers(String playerId, Effect effect) {
            _playerId = playerId;
            _effect = effect;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame.getGameState(), physicalCard)) {
                List<? extends Action> actions = physicalCard.getBlueprint().getOptionalBeforeTriggers(_playerId, _lotroGame, _effect, physicalCard);
                if (actions != null)
                    _actions.addAll(actions);
            }
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherOptionalAfterActions extends CompletePhysicalCardVisitor {
        private String _playerId;
        private Collection<? extends EffectResult> _effectResults;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherOptionalAfterActions(String playerId, Collection<? extends EffectResult> effectResults) {
            _playerId = playerId;
            _effectResults = effectResults;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame.getGameState(), physicalCard)) {
                if (_effectResults != null)
                    for (EffectResult effectResult : _effectResults) {
                        List<? extends Action> actions = physicalCard.getBlueprint().getOptionalAfterActions(_playerId, _lotroGame, effectResult, physicalCard);
                        if (actions != null)
                            _actions.addAll(actions);
                    }
            }
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherPhaseActionsVisitor extends CompletePhysicalCardVisitor {
        private LotroGame _game;
        private String _playerId;

        private List<Action> _actions = new LinkedList<Action>();

        public GatherPhaseActionsVisitor(LotroGame game, String playerId) {
            _game = game;
            _playerId = playerId;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame.getGameState(), physicalCard)) {
                List<? extends Action> normalActions = physicalCard.getBlueprint().getPhaseActions(_playerId, _game, physicalCard);
                if (normalActions != null) {
                    for (Action action : normalActions) {
                        if (action != null)
                            _actions.add(action);
                        else
                            System.out.println("Null action from: " + physicalCard.getBlueprint().getName());
                    }
                }
                final List<? extends ActivateCardAction> extraActions = _game.getModifiersQuerying().getExtraPhaseActions(_game.getGameState(), physicalCard);
                if (extraActions != null) {
                    for (Action action : extraActions) {
                        if (action != null)
                            _actions.add(action);
                        else
                            System.out.println("Null action from: " + physicalCard.getBlueprint().getName());
                    }
                }
            }
        }

        public List<? extends Action> getActions() {
            return _actions;
        }
    }
}
