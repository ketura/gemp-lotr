package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
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
                Effect effect = _actionStack.getNextEffect();
                if (effect != null) {
                    if (effect.getType() == null) {
                        if (effect.canPlayEffect(_game))
                            effect.playEffect(_game);
                        else
                            effect.setFailed();
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
        private EffectResult _effectResult;
        private boolean _checkedPlayability;
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
        public Effect nextEffect() {
            if (_effect.isCancelled())
                return null;

            if (!_checkedPlayability) {
                boolean canBePlayed = _effect.canPlayEffect(_game);
                _checkedPlayability = true;
                if (!canBePlayed) {
                    _effect.setFailed();
                    // This finished off this Action
                    return null;
                }
            }
            if (!_checkedIsAboutToRequiredResponses) {
                _checkedIsAboutToRequiredResponses = true;
                List<Action> requiredIsAboutToResponses = _game.getActionsEnvironment().getRequiredBeforeTriggers(_effect);
                if (requiredIsAboutToResponses.size() > 0) {
                    DefaultCostToEffectAction action = new DefaultCostToEffectAction(null, null, null);
                    action.addEffect(new PlayoutAllActionsIfEffectNotCancelledEffect(action, _effect, requiredIsAboutToResponses));
                    return new StackActionEffect(action);
                }
            }
            if (!_checkedIsAboutToOptionalResponses) {
                _checkedIsAboutToOptionalResponses = true;
                DefaultCostToEffectAction action = new DefaultCostToEffectAction(null, null, null);
                action.addEffect(new PlayoutOptionalBeforeResponsesEffect(action, new HashSet<PhysicalCard>(), _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0, _effect));
                return new StackActionEffect(action);
            }
            if (!_effectPlayed) {
                if (_effect.canPlayEffect(_game))
                    _effectResult = _effect.playEffect(_game);
                else {
                    _effect.setFailed();
                    return null;
                }
                _effectPlayed = true;
            }
            if (_effectResult != null) {
                if (!_checkedRequiredWhenResponses) {
                    _checkedRequiredWhenResponses = true;
                    List<Action> requiredResponses = _game.getActionsEnvironment().getRequiredAfterTriggers(_effectResult);
                    if (requiredResponses.size() > 0) {
                        DefaultCostToEffectAction action = new DefaultCostToEffectAction(null, null, null);
                        action.addEffect(new PlayoutAllActionsIfEffectNotCancelledEffect(action, _effect, requiredResponses));
                        return new StackActionEffect(action);
                    }
                }
                if (!_checkedOptionalWhenResponses) {
                    _checkedOptionalWhenResponses = true;
                    DefaultCostToEffectAction action = new DefaultCostToEffectAction(null, null, null);
                    action.addEffect(
                            new PlayoutOptionalAfterResponsesEffect(action, new HashSet<PhysicalCard>(), _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0, _effect, _effectResult));
                    return new StackActionEffect(action);
                }
            }

            return null;
        }
    }

    private class PlayoutOptionalBeforeResponsesEffect extends UnrespondableEffect {
        private DefaultCostToEffectAction _action;
        private Set<PhysicalCard> _cardTriggersUsed;
        private PlayOrder _playOrder;
        private int _passCount;
        private Effect _effect;

        private PlayoutOptionalBeforeResponsesEffect(DefaultCostToEffectAction action, Set<PhysicalCard> cardTriggersUsed, PlayOrder playOrder, int passCount, Effect effect) {
            _action = action;
            _cardTriggersUsed = cardTriggersUsed;
            _playOrder = playOrder;
            _passCount = passCount;
            _effect = effect;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            if (!_effect.isCancelled()) {
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
                                        _action.addEffect(new PlayoutOptionalBeforeResponsesEffect(_action, _cardTriggersUsed, _playOrder, 0, _effect));
                                    } else {
                                        if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                                            _action.addEffect(new PlayoutOptionalBeforeResponsesEffect(_action, _cardTriggersUsed, _playOrder, _passCount + 1, _effect));
                                        }
                                    }
                                }
                            });
                } else {
                    if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                        _action.addEffect(new PlayoutOptionalBeforeResponsesEffect(_action, _cardTriggersUsed, _playOrder, _passCount + 1, _effect));
                    }
                }
            }
        }
    }

    private class PlayoutOptionalAfterResponsesEffect extends UnrespondableEffect {
        private DefaultCostToEffectAction _action;
        private Set<PhysicalCard> _cardTriggersUsed;
        private PlayOrder _playOrder;
        private int _passCount;
        private Effect _effect;
        private EffectResult _effectResult;

        private PlayoutOptionalAfterResponsesEffect(DefaultCostToEffectAction action, Set<PhysicalCard> cardTriggersUsed, PlayOrder playOrder, int passCount, Effect effect, EffectResult effectResult) {
            _action = action;
            _cardTriggersUsed = cardTriggersUsed;
            _playOrder = playOrder;
            _passCount = passCount;
            _effect = effect;
            _effectResult = effectResult;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            final String activePlayer = _playOrder.getNextPlayer();

            final List<Action> optionalAfterTriggers = game.getActionsEnvironment().getOptionalAfterTriggers(activePlayer, _effectResult);
            // Remove triggers already resolved
            final Iterator<Action> triggersIterator = optionalAfterTriggers.iterator();
            while (triggersIterator.hasNext())
                if (_cardTriggersUsed.contains(triggersIterator.next().getActionSource()))
                    triggersIterator.remove();

            final List<Action> optionalAfterActions = _game.getActionsEnvironment().getOptionalAfterActions(activePlayer, _effectResult);

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
                                        _cardTriggersUsed.add(action.getActionSource());
                                    _action.addEffect(new PlayoutOptionalAfterResponsesEffect(_action, _cardTriggersUsed, _playOrder, 0, _effect, _effectResult));
                                } else {
                                    if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                                        _action.addEffect(new PlayoutOptionalAfterResponsesEffect(_action, _cardTriggersUsed, _playOrder, _passCount + 1, _effect, _effectResult));
                                    }
                                }
                            }
                        });
            } else {
                if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                    _action.addEffect(new PlayoutOptionalAfterResponsesEffect(_action, _cardTriggersUsed, _playOrder, _passCount + 1, _effect, _effectResult));
                }
            }
        }
    }

    private class PlayoutAllActionsIfEffectNotCancelledEffect extends UnrespondableEffect {
        private DefaultCostToEffectAction _action;
        private Effect _effect;
        private List<Action> _actions;

        private PlayoutAllActionsIfEffectNotCancelledEffect(DefaultCostToEffectAction action, Effect effect, List<Action> actions) {
            _action = action;
            _effect = effect;
            _actions = actions;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            if (!_effect.isCancelled()) {
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
                                    _action.addEffect(new PlayoutAllActionsIfEffectNotCancelledEffect(_action, _effect, _actions));
                                }
                            });
                }
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
