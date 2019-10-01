package com.gempukku.lotro.game.state.actions;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.ActionStack;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.processes.GatherPlayableActionsFromDiscardVisitor;
import com.gempukku.lotro.logic.timing.results.AssignedToSkirmishResult;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;
import org.apache.log4j.Logger;

import java.util.*;

public class DefaultActionsEnvironment implements ActionsEnvironment {
    private static Logger LOG = Logger.getLogger(DefaultActionsEnvironment.class);
    private LotroGame _lotroGame;
    private ActionStack _actionStack;
    private List<ActionProxy> _actionProxies = new LinkedList<ActionProxy>();
    private Map<Phase, List<ActionProxy>> _untilStartOfPhaseActionProxies = new HashMap<Phase, List<ActionProxy>>();
    private Map<Phase, List<ActionProxy>> _untilEndOfPhaseActionProxies = new HashMap<Phase, List<ActionProxy>>();
    private List<ActionProxy> _untilEndOfTurnActionProxies = new LinkedList<ActionProxy>();

    private List<PhysicalCard> _playedCardsInPhase = new LinkedList<PhysicalCard>();
    private List<PhysicalCard> _playedCardsInTurn = new LinkedList<PhysicalCard>();

    private Set<EffectResult> _effectResults = new HashSet<EffectResult>();

    private Set<PhysicalCard> _wonSkirmishesInTurn = new HashSet<PhysicalCard>();
    private Set<PhysicalCard> _lostSkirmishesInTurn = new HashSet<PhysicalCard>();
    private Set<PhysicalCard> _assignedInTurn = new HashSet<PhysicalCard>();

    public DefaultActionsEnvironment(LotroGame lotroGame, ActionStack actionStack) {
        _lotroGame = lotroGame;
        _actionStack = actionStack;

        addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResults) {
                        if (effectResults.getType() == EffectResult.Type.PLAY) {
                            PlayCardResult playResult = (PlayCardResult) effectResults;
                            _playedCardsInPhase.add(playResult.getPlayedCard());
                            _playedCardsInTurn.add(playResult.getPlayedCard());
                        }
                        return null;
                    }
                });
        addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.CHARACTER_WON_SKIRMISH) {
                            final CharacterWonSkirmishResult winResult = (CharacterWonSkirmishResult) effectResult;
                            _wonSkirmishesInTurn.add(winResult.getWinner());
                        }
                        return null;
                    }
                });
        addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.CHARACTER_LOST_SKIRMISH) {
                            final CharacterLostSkirmishResult winResult = (CharacterLostSkirmishResult) effectResult;
                            _lostSkirmishesInTurn.add(winResult.getLoser());
                        }
                        return null;
                    }
                });
        addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.ASSIGNED_TO_SKIRMISH) {
                            AssignedToSkirmishResult assignResult = (AssignedToSkirmishResult) effectResult;
                            _assignedInTurn.add(assignResult.getAssignedCard());
                        }
                        return null;
                    }
                }
        );
    }

    public List<ActionProxy> getUntilStartOfPhaseActionProxies(Phase phase) {
        return _untilStartOfPhaseActionProxies.get(phase);
    }

    @Override
    public void emitEffectResult(EffectResult effectResult) {
        _effectResults.add(effectResult);
    }

    public Set<EffectResult> consumeEffectResults() {
        Set<EffectResult> result = _effectResults;
        _effectResults = new HashSet<EffectResult>();
        return result;
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
        _playedCardsInPhase.clear();
    }

    public void removeEndOfTurnActionProxies() {
        _actionProxies.removeAll(_untilEndOfTurnActionProxies);
        _untilEndOfTurnActionProxies.clear();
        _wonSkirmishesInTurn.clear();
        _lostSkirmishesInTurn.clear();
        _assignedInTurn.clear();
        _playedCardsInTurn.clear();
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
            List<? extends RequiredTriggerAction> actions = actionProxy.getRequiredBeforeTriggers(_lotroGame, effect);
            if (actions != null) {
                gatheredActions.addAll(actions);
            }
        }

        return gatheredActions;
    }

    @Override
    public List<Action> getOptionalBeforeTriggers(String playerId, Effect effect) {
        GatherOptionalBeforeTriggers gatherActions = new GatherOptionalBeforeTriggers(playerId, effect);

        _lotroGame.getGameState().iterateActiveTextCards(playerId, gatherActions);

        List<Action> actionList = gatherActions.getActions();

        for (ActionProxy actionProxy : _actionProxies) {
            List<? extends OptionalTriggerAction> actions = actionProxy.getOptionalBeforeTriggers(playerId, _lotroGame, effect);
            if (actions != null) {
                actionList.addAll(actions);
            }
        }

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
            if (_lotroGame.getModifiersQuerying().canPlayAction(_lotroGame, playerId, action))
                result.add(action);
        }

        return result;
    }

    @Override
    public List<Action> getRequiredAfterTriggers(Collection<? extends EffectResult> effectResults) {
        GatherRequiredAfterTriggers gatherActions = new GatherRequiredAfterTriggers(effectResults);

        _lotroGame.getGameState().iterateActiveTextCards(gatherActions);

        List<Action> gatheredActions = gatherActions.getActions();

        if (effectResults != null) {
            for (ActionProxy actionProxy : _actionProxies) {
                for (EffectResult effectResult : effectResults) {
                    List<? extends RequiredTriggerAction> actions = actionProxy.getRequiredAfterTriggers(_lotroGame, effectResult);
                    if (actions != null)
                        gatheredActions.addAll(actions);
                }
            }
        }

        return gatheredActions;
    }

    @Override
    public Map<OptionalTriggerAction, EffectResult> getOptionalAfterTriggers(String playerId, Collection<? extends EffectResult> effectResults) {
        final boolean fpPlayer = GameUtils.isFP(_lotroGame, playerId);
        GatherOptionalAfterTriggers gatherActions = new GatherOptionalAfterTriggers(playerId, effectResults);

        _lotroGame.getGameState().iterateActiveTextCards(playerId, gatherActions);

        final Map<OptionalTriggerAction, EffectResult> gatheredActions = gatherActions.getActions();

        if (effectResults != null) {
            for (ActionProxy actionProxy : _actionProxies) {
                for (EffectResult effectResult : effectResults) {
                    List<? extends OptionalTriggerAction> actions = actionProxy.getOptionalAfterTriggers(playerId, _lotroGame, effectResult);
                    if (actions != null) {
                        for (OptionalTriggerAction action : actions)
                            if (!effectResult.wasOptionalTriggerUsed(action))
                                gatheredActions.put(action, effectResult);
                    }
                }
            }

            Side workingSide = fpPlayer ? Side.FREE_PEOPLE : Side.SHADOW;

            // Optional triggers from hand
            for (PhysicalCard cardInHand : _lotroGame.getGameState().getHand(playerId)) {
                if (cardInHand.getBlueprint().getSide() == workingSide) {
                    for (EffectResult effectResult : effectResults) {
                        List<OptionalTriggerAction> actions = cardInHand.getBlueprint().getOptionalInHandAfterTriggers(playerId, _lotroGame, effectResult, cardInHand);
                        if (actions != null) {
                            for (OptionalTriggerAction action : actions) {
                                if (!effectResult.wasOptionalTriggerUsed(action))
                                    gatheredActions.put(action, effectResult);
                            }
                        }
                    }
                }
            }
        }

        for (Action gatheredAction : gatheredActions.keySet())
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
            if (_lotroGame.getModifiersQuerying().canPlayAction(_lotroGame, playerId, action))
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
            if (_lotroGame.getModifiersQuerying().canPlayAction(_lotroGame, playerId, action))
                playableActions.add(action);
        }

        for (Action action : stackedVisitor.getActions()) {
            action.setActionTimeword(_lotroGame.getGameState().getCurrentPhase());
            action.setPerformingPlayer(playerId);
            if (_lotroGame.getModifiersQuerying().canPlayAction(_lotroGame, playerId, action))
                playableActions.add(action);
        }

        for (Action action : discardVisitor.getActions()) {
            action.setActionTimeword(_lotroGame.getGameState().getCurrentPhase());
            action.setPerformingPlayer(playerId);
            if (_lotroGame.getModifiersQuerying().canPlayAction(_lotroGame, playerId, action))
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

    @Override
    public List<PhysicalCard> getPlayedCardsInCurrentPhase() {
        return Collections.unmodifiableList(_playedCardsInPhase);
    }

    @Override
    public List<PhysicalCard> getPlayedCardsInCurrentTurn() {
        return Collections.unmodifiableList(_playedCardsInTurn);
    }

    @Override
    public boolean hasWonSkirmishThisTurn(LotroGame game, Filterable... filters) {
        return Filters.filter(_wonSkirmishesInTurn, game, filters).size() > 0;
    }

    @Override
    public boolean hasLostSkirmishThisTurn(LotroGame game, Filterable... filters) {
        return Filters.filter(_lostSkirmishesInTurn, game, filters).size() > 0;
    }

    @Override
    public boolean wasAssignedThisTurn(LotroGame game, Filterable... filters) {
        return Filters.filter(_assignedInTurn, game, filters).size() > 0;
    }

    private class GatherRequiredAfterTriggers extends CompletePhysicalCardVisitor {
        private Collection<? extends EffectResult> _effectResults;
        private List<Action> _actions = new LinkedList<Action>();

        private GatherRequiredAfterTriggers(Collection<? extends EffectResult> effectResults) {
            _effectResults = effectResults;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame, physicalCard)) {
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
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame, physicalCard)) {
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
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame, physicalCard)) {
                if (physicalCard.getZone().isInPlay()) {
                    List<? extends Action> actions = physicalCard.getBlueprint().getOptionalInPlayBeforeActions(_playerId, _lotroGame, _effect, physicalCard);
                    if (actions != null)
                        _actions.addAll(actions);
                } else if (physicalCard.getZone() == Zone.HAND
                        && PlayUtils.checkPlayRequirements(_lotroGame, physicalCard, Filters.any, 0, 0, false, false)) {
                    List<? extends Action> actions = physicalCard.getBlueprint().getPlayResponseEventBeforeActions(_playerId, _lotroGame, _effect, physicalCard);
                    if (actions != null)
                        _actions.addAll(actions);
                }
            }
        }

        public List<Action> getActions() {
            return _actions;
        }
    }

    private class GatherOptionalAfterTriggers extends CompletePhysicalCardVisitor {
        private String _playerId;
        private Collection<? extends EffectResult> _effectResults;
        private Map<OptionalTriggerAction, EffectResult> _actions = new HashMap<OptionalTriggerAction, EffectResult>();

        private GatherOptionalAfterTriggers(String playerId, Collection<? extends EffectResult> effectResults) {
            _playerId = playerId;
            _effectResults = effectResults;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame, physicalCard)) {
                if (_effectResults != null)
                    for (EffectResult effectResult : _effectResults) {
                        List<OptionalTriggerAction> actions = physicalCard.getBlueprint().getOptionalAfterTriggers(_playerId, _lotroGame, effectResult, physicalCard);
                        if (actions != null) {
                            for (OptionalTriggerAction action : actions)
                                if (!effectResult.wasOptionalTriggerUsed(action))
                                    _actions.put(action, effectResult);
                        }
                    }
            }
        }

        public Map<OptionalTriggerAction, EffectResult> getActions() {
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
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame, physicalCard)) {
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
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame, physicalCard)) {
                if (_effectResults != null)
                    for (EffectResult effectResult : _effectResults) {
                        if (physicalCard.getZone().isInPlay()) {
                            List<? extends Action> actions = physicalCard.getBlueprint().getOptionalInPlayAfterActions(_playerId, _lotroGame, effectResult, physicalCard);
                            if (actions != null)
                                _actions.addAll(actions);
                        } else if (physicalCard.getZone() == Zone.HAND
                                && PlayUtils.checkPlayRequirements(_lotroGame, physicalCard, Filters.any, 0, 0, false, false)) {
                            List<? extends Action> actions = physicalCard.getBlueprint().getPlayResponseEventAfterActions(_playerId, _lotroGame, effectResult, physicalCard);
                            if (actions != null)
                                _actions.addAll(actions);
                        }
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
            if (!_lotroGame.getModifiersQuerying().hasTextRemoved(_lotroGame, physicalCard)) {
                if (physicalCard.getZone().isInPlay()) {
                    List<? extends Action> normalActions = physicalCard.getBlueprint().getPhaseActionsInPlay(_playerId, _game, physicalCard);
                    if (normalActions != null) {
                        for (Action action : normalActions) {
                            if (action != null)
                                _actions.add(action);
                            else
                                LOG.error("Null action from: " + physicalCard.getBlueprint().getTitle());
                        }
                    }
                } else if (physicalCard.getZone() == Zone.HAND) {
                    List<? extends Action> normalActions = physicalCard.getBlueprint().getPhaseActionsInHand(_playerId, _game, physicalCard);
                    if (normalActions != null) {
                        for (Action action : normalActions) {
                            if (action != null)
                                _actions.add(action);
                            else
                                LOG.error("Null action from: " + physicalCard.getBlueprint().getTitle());
                        }
                    }
                }
                final List<? extends Action> extraActions = _game.getModifiersQuerying().getExtraPhaseActions(_game, physicalCard);
                if (extraActions != null) {
                    for (Action action : extraActions) {
                        if (action != null)
                            _actions.add(action);
                        else
                            LOG.debug("Null action from: " + physicalCard.getBlueprint().getTitle());
                    }
                }
            }
        }

        public List<? extends Action> getActions() {
            return _actions;
        }
    }

    private class GatherPlayableActionsFromStackedVisitor extends CompletePhysicalCardVisitor {
        private LotroGame _game;
        private String _playerId;

        private List<Action> _actions = new LinkedList<Action>();

        public GatherPlayableActionsFromStackedVisitor(LotroGame game, String playerId) {
            _game = game;
            _playerId = playerId;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            List<? extends Action> list = physicalCard.getBlueprint().getPhaseActionsFromStacked(_playerId, _game, physicalCard);
            if (list != null)
                _actions.addAll(list);
            final List<? extends Action> extraActions = _game.getModifiersQuerying().getExtraPhaseActionsFromStacked(_game, physicalCard);
            if (extraActions != null) {
                for (Action action : extraActions) {
                    if (action != null)
                        _actions.add(action);
                    else
                        LOG.debug("Null action from: " + physicalCard.getBlueprint().getTitle());
                }
            }
        }

        public List<? extends Action> getActions() {
            return _actions;
        }
    }

}
