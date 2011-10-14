package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.decisions.ActionSelectionDecision;
import com.gempukku.lotro.logic.decisions.CardActionSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.actions.SystemAction;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.pregame.BiddingGameProcess;

import java.util.*;

// Action generates multiple Effects, both costs and result of an action are Effects.

// Decision is also an Effect.
public class TurnProcedure {
    private UserFeedback _userFeedback;
    private LotroGame _game;
    private ActionStack _actionStack;
    private GameProcess _gameProcess;
    private boolean _playedGameProcess;

    public TurnProcedure(LotroGame lotroGame, Set<String> players, final UserFeedback userFeedback, ActionStack actionStack, final PlayerOrderFeedback playerOrderFeedback) {
        _userFeedback = userFeedback;
        _game = lotroGame;
        _actionStack = actionStack;

        _gameProcess = new BiddingGameProcess(players, lotroGame, playerOrderFeedback);
    }

    public void carryOutPendingActionsUntilDecisionNeeded() {
        while (!_userFeedback.hasPendingDecisions() && _game.getWinnerPlayerId() == null) {
            if (_actionStack.isEmpty()) {
                if (_playedGameProcess) {
                    _gameProcess = _gameProcess.getNextProcess();
                    _playedGameProcess = false;
                } else {
                    _gameProcess.process();
                    _playedGameProcess = true;
                }
            } else {
                Effect effect = _actionStack.getNextEffect(_game);
                if (effect != null) {
                    if (effect.getType() == null) {
                        effect.playEffect(_game);
                    } else {
                        _actionStack.stackAction(new PlayOutRecognizableEffect(effect));
                    }
                }
            }
            _game.checkLoseConditions();
        }
    }

    private class PlayOutRecognizableEffect extends SystemAction {
        private Effect _effect;
        private EffectResult[] _effectResults;
        private boolean _checkedIsAboutToRequiredResponses;
        private boolean _checkedIsAboutToOptionalResponses;
        private boolean _effectPlayed;
        private boolean _checkedRequiredWhenResponses;
        private boolean _checkedOptionalWhenResponses;

        private PlayOutRecognizableEffect(Effect effect) {
            _effect = effect;
        }

        @Override
        public String getText(LotroGame game) {
            return _effect.getText(game);
        }

        @Override
        public Effect nextEffect(LotroGame game) {
            if (!_checkedIsAboutToRequiredResponses) {
                _checkedIsAboutToRequiredResponses = true;
                List<Action> requiredIsAboutToResponses = _game.getActionsEnvironment().getRequiredBeforeTriggers(_effect);
                if (requiredIsAboutToResponses.size() > 0) {
                    SystemQueueAction action = new SystemQueueAction();
                    action.appendEffect(new PlayoutAllActionsIfEffectNotCancelledEffect(action, _effect, requiredIsAboutToResponses));
                    return new StackActionEffect(action);
                }
            }
            if (!_checkedIsAboutToOptionalResponses) {
                _checkedIsAboutToOptionalResponses = true;
                SystemQueueAction action = new SystemQueueAction();
                action.appendEffect(new PlayoutOptionalBeforeResponsesEffect(action, new HashSet<PhysicalCard>(), _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0, _effect));
                return new StackActionEffect(action);
            }
            if (!_effectPlayed) {
                _effectResults = _effect.playEffect(_game);
                _effectPlayed = true;
            }
            if (_effectResults != null) {
                if (!_checkedRequiredWhenResponses) {
                    _checkedRequiredWhenResponses = true;
                    List<Action> requiredResponses = _game.getActionsEnvironment().getRequiredAfterTriggers(_effectResults);
                    if (requiredResponses.size() > 0) {
                        SystemQueueAction action = new SystemQueueAction();
                        action.appendEffect(new PlayoutAllActionsIfEffectNotCancelledEffect(action, _effect, requiredResponses));
                        return new StackActionEffect(action);
                    }
                }
                if (!_checkedOptionalWhenResponses) {
                    _checkedOptionalWhenResponses = true;
                    SystemQueueAction action = new SystemQueueAction();

                    Map<String, List<Action>> optionalAfterTriggers = new HashMap<String, List<Action>>();
                    for (String playerId : _game.getGameState().getPlayerOrder().getAllPlayers()) {
                        List<Action> actions = new LinkedList<Action>();
                        actions.addAll(_game.getActionsEnvironment().getOptionalAfterTriggers(playerId, _effectResults));
                        optionalAfterTriggers.put(playerId, actions);
                    }

                    action.appendEffect(
                            new PlayoutOptionalAfterResponsesEffect(action, optionalAfterTriggers, _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0, _effect, _effectResults));
                    return new StackActionEffect(action);
                }
            }

            return null;
        }
    }

    private class PlayoutOptionalBeforeResponsesEffect extends UnrespondableEffect {
        private SystemQueueAction _action;
        private Set<PhysicalCard> _cardTriggersUsed;
        private PlayOrder _playOrder;
        private int _passCount;
        private Effect _effect;

        private PlayoutOptionalBeforeResponsesEffect(SystemQueueAction action, Set<PhysicalCard> cardTriggersUsed, PlayOrder playOrder, int passCount, Effect effect) {
            _action = action;
            _cardTriggersUsed = cardTriggersUsed;
            _playOrder = playOrder;
            _passCount = passCount;
            _effect = effect;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            final String activePlayer = _playOrder.getNextPlayer();

            final List<Action> optionalBeforeTriggers = game.getActionsEnvironment().getOptionalBeforeTriggers(activePlayer, _effect);
            // Remove triggers already resolved
            final Iterator<Action> triggersIterator = optionalBeforeTriggers.iterator();
            while (triggersIterator.hasNext())
                if (_cardTriggersUsed.contains(triggersIterator.next().getActionSource()))
                    triggersIterator.remove();

            final List<Action> optionalBeforeActions = _game.getActionsEnvironment().getOptionalBeforeActions(activePlayer, _effect);

            List<Action> possibleActions = new LinkedList<Action>(optionalBeforeTriggers);
            possibleActions.addAll(optionalBeforeActions);

            if (possibleActions.size() > 0) {
                _game.getUserFeedback().sendAwaitingDecision(activePlayer,
                        new CardActionSelectionDecision(game, 1, _effect.getText(game) + " - Optional \"is about to\" responses", possibleActions, true) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                Action action = getSelectedAction(result);
                                if (action != null) {
                                    _game.getActionsEnvironment().addActionToStack(action);
                                    if (optionalBeforeTriggers.contains(action))
                                        _cardTriggersUsed.add(action.getActionSource());
                                    _action.appendEffect(new PlayoutOptionalBeforeResponsesEffect(_action, _cardTriggersUsed, _playOrder, 0, _effect));
                                } else {
                                    if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                                        _action.appendEffect(new PlayoutOptionalBeforeResponsesEffect(_action, _cardTriggersUsed, _playOrder, _passCount + 1, _effect));
                                    }
                                }
                            }
                        });
            } else {
                if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                    _action.appendEffect(new PlayoutOptionalBeforeResponsesEffect(_action, _cardTriggersUsed, _playOrder, _passCount + 1, _effect));
                }
            }
        }
    }

    private class PlayoutOptionalAfterResponsesEffect extends UnrespondableEffect {
        private SystemQueueAction _action;
        private Map<String, List<Action>> _unplayedAfterTriggers;
        private PlayOrder _playOrder;
        private int _passCount;
        private Effect _effect;
        private EffectResult[] _effectResults;

        private PlayoutOptionalAfterResponsesEffect(SystemQueueAction action, Map<String, List<Action>> unplayedAfterTriggers, PlayOrder playOrder, int passCount, Effect effect, EffectResult[] effectResults) {
            _action = action;
            _unplayedAfterTriggers = unplayedAfterTriggers;
            _playOrder = playOrder;
            _passCount = passCount;
            _effect = effect;
            _effectResults = effectResults;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            final String activePlayer = _playOrder.getNextPlayer();

            final List<Action> optionalAfterTriggers = _unplayedAfterTriggers.get(activePlayer);

            // Remove triggers for cards no longer in play
            final Iterator<Action> triggersIterator = optionalAfterTriggers.iterator();
            while (triggersIterator.hasNext()) {
                final PhysicalCard actionSource = triggersIterator.next().getActionSource();
                if (!actionSource.getZone().isInPlay())
                    triggersIterator.remove();
            }

            final List<Action> optionalAfterActions = _game.getActionsEnvironment().getOptionalAfterActions(activePlayer, _effectResults);

            List<Action> possibleActions = new LinkedList<Action>(optionalAfterTriggers);
            possibleActions.addAll(optionalAfterActions);

            if (possibleActions.size() > 0) {
                _game.getUserFeedback().sendAwaitingDecision(activePlayer,
                        new CardActionSelectionDecision(game, 1, _effect.getText(game) + " - optional \"when\" responses", possibleActions, true) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                Action action = getSelectedAction(result);
                                if (action != null) {
                                    _game.getActionsEnvironment().addActionToStack(action);
                                    if (optionalAfterTriggers.contains(action))
                                        optionalAfterTriggers.remove(action);
                                    _action.appendEffect(new PlayoutOptionalAfterResponsesEffect(_action, _unplayedAfterTriggers, _playOrder, 0, _effect, _effectResults));
                                } else {
                                    if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                                        _action.appendEffect(new PlayoutOptionalAfterResponsesEffect(_action, _unplayedAfterTriggers, _playOrder, _passCount + 1, _effect, _effectResults));
                                    }
                                }
                            }
                        });
            } else {
                if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                    _action.appendEffect(new PlayoutOptionalAfterResponsesEffect(_action, _unplayedAfterTriggers, _playOrder, _passCount + 1, _effect, _effectResults));
                }
            }
        }
    }

    private class PlayoutAllActionsIfEffectNotCancelledEffect extends UnrespondableEffect {
        private SystemQueueAction _action;
        private Effect _effect;
        private List<Action> _actions;

        private PlayoutAllActionsIfEffectNotCancelledEffect(SystemQueueAction action, Effect effect, List<Action> actions) {
            _action = action;
            _effect = effect;
            _actions = actions;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            if (_actions.size() == 1) {
                _game.getActionsEnvironment().addActionToStack(_actions.get(0));
            } else {
                _game.getUserFeedback().sendAwaitingDecision(_game.getGameState().getCurrentPlayerId(),
                        new ActionSelectionDecision(_game, 1, _effect.getText(game) + " - required responses", _actions) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                Action action = getSelectedAction(result);
                                _game.getActionsEnvironment().addActionToStack(action);
                                _actions.remove(action);
                                _action.appendEffect(new PlayoutAllActionsIfEffectNotCancelledEffect(_action, _effect, _actions));
                            }
                        });
            }
        }
    }

    private class StackActionEffect extends UnrespondableEffect {
        private Action _action;

        private StackActionEffect(Action action) {
            _action = action;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            _actionStack.stackAction(_action);
        }
    }
}
