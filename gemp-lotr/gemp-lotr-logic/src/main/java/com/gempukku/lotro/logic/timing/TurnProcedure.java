package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.decisions.ActionSelectionDecision;
import com.gempukku.lotro.logic.decisions.CardActionSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.pregame.BiddingGameProcess;
import com.gempukku.lotro.logic.timing.results.KilledResult;
import com.gempukku.lotro.logic.timing.rules.CharacterDeathRule;
import com.gempukku.lotro.logic.timing.rules.InitiativeChangeRule;

import java.util.*;

// Action generates multiple Effects, both costs and result of an action are Effects.

// Decision is also an Effect.
public class TurnProcedure {
    private UserFeedback _userFeedback;
    private LotroGame _game;
    private ActionStack _actionStack;
    private CharacterDeathRule _characterDeathRule;
    private GameProcess _gameProcess;
    private boolean _playedGameProcess;
    private GameStats _gameStats;
    private InitiativeChangeRule _initiativeChangeRule = new InitiativeChangeRule();

    public TurnProcedure(LotroGame lotroGame, Set<String> players, final UserFeedback userFeedback, ActionStack actionStack, final PlayerOrderFeedback playerOrderFeedback, CharacterDeathRule characterDeathRule) {
        _userFeedback = userFeedback;
        _game = lotroGame;
        _actionStack = actionStack;
        _characterDeathRule = characterDeathRule;

        _gameStats = new GameStats();

        _gameProcess = new BiddingGameProcess(players, playerOrderFeedback);
    }

    public GameStats getGameStats() {
        return _gameStats;
    }

    public void carryOutPendingActionsUntilDecisionNeeded() {
        while (!_userFeedback.hasPendingDecisions() && _game.getWinnerPlayerId() == null) {
            // First check for any "state-based" effects
            _initiativeChangeRule.checkInitiativeChange(_game);
            _characterDeathRule.checkCharactersZeroVitality(_game);

            Set<EffectResult> effectResults = ((DefaultActionsEnvironment) _game.getActionsEnvironment()).consumeEffectResults();
            if (effectResults.size() > 0) {
                _actionStack.stackAction(new PlayOutEffectResults(effectResults));
            } else {
                if (_actionStack.isEmpty()) {
                    if (_playedGameProcess) {
                        _gameProcess = _gameProcess.getNextProcess();
                        _playedGameProcess = false;
                    } else {
                        _gameProcess.process(_game);
                        if (_gameStats.updateGameStats(_game))
                            _game.getGameState().sendGameStats(_gameStats);
                        _playedGameProcess = true;
                    }
                } else {
                    Effect effect = _actionStack.getNextEffect(_game);
                    if (effect != null) {
                        if (effect.getType() == null)
                            effect.playEffect(_game);
                        else
                            _actionStack.stackAction(new PlayOutEffect(effect));
                    }
                }
            }

            if (_gameStats.updateGameStats(_game))
                _game.getGameState().sendGameStats(_gameStats);

            _game.checkRingBearerCorruption();
        }
    }

    private class PlayOutEffect extends SystemQueueAction {
        private Effect _effect;
        private boolean _initialized;

        private PlayOutEffect(Effect effect) {
            _effect = effect;
        }

        @Override
        public String getText(LotroGame game) {
            return _effect.getText(game);
        }

        @Override
        public Effect nextEffect(LotroGame game) {
            if (!_initialized) {
                _initialized = true;
                List<Action> requiredIsAboutToResponses = _game.getActionsEnvironment().getRequiredBeforeTriggers(_effect);
                if (requiredIsAboutToResponses.size() > 0)
                    appendEffect(new PlayoutAllActionsIfEffectNotCancelledEffect(this, requiredIsAboutToResponses));
                appendEffect(new PlayoutOptionalBeforeResponsesEffect(this, new HashSet<PhysicalCard>(), _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0, _effect));
                appendEffect(new PlayEffect(_effect));
            }

            return getNextEffect();
        }
    }

    private class PlayEffect extends UnrespondableEffect {
        private Effect _effect;

        private PlayEffect(Effect effect) {
            _effect = effect;
        }

        @Override
        protected void doPlayEffect(LotroGame game) {
            _effect.playEffect(game);
        }
    }

    private class PlayOutEffectResults extends SystemQueueAction {
        private Set<EffectResult> _effectResults;
        private boolean _initialized;

        private PlayOutEffectResults(Set<EffectResult> effectResults) {
            _effectResults = effectResults;
        }

        @Override
        public Effect nextEffect(LotroGame game) {
            if (!_initialized) {
                _initialized = true;
                List<Action> requiredResponses = _game.getActionsEnvironment().getRequiredAfterTriggers(_effectResults);
                if (requiredResponses.size() > 0)
                    appendEffect(new PlayoutAllActionsIfEffectNotCancelledEffect(this, requiredResponses));

                appendEffect(
                        new PlayoutOptionalAfterResponsesEffect(this, _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0, _effectResults));
                appendEffect(
                        new UnrespondableEffect() {
                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                if (hasKilledRingBearer())
                                    _game.checkRingBearerAlive();
                            }
                        });
            }
            return getNextEffect();
        }

        private boolean hasKilledRingBearer() {
            for (EffectResult effectResult : _effectResults) {
                if (effectResult.getType() == EffectResult.Type.ANY_NUMBER_KILLED) {
                    KilledResult killResult = (KilledResult) effectResult;
                    if (Filters.filter(killResult.getKilledCards(), _game.getGameState(), _game.getModifiersQuerying(), Filters.ringBearer).size() > 0)
                        return true;
                }
            }
            return false;
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
                        new CardActionSelectionDecision(game, 1, _effect.getText(game) + " - Optional \"is about to\" responses", possibleActions) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                Action action = getSelectedAction(result);
                                if (action != null) {
                                    _game.getActionsEnvironment().addActionToStack(action);
                                    if (optionalBeforeTriggers.contains(action))
                                        _cardTriggersUsed.add(action.getActionSource());
                                    _action.insertEffect(new PlayoutOptionalBeforeResponsesEffect(_action, _cardTriggersUsed, _playOrder, 0, _effect));
                                } else {
                                    if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                                        _action.insertEffect(new PlayoutOptionalBeforeResponsesEffect(_action, _cardTriggersUsed, _playOrder, _passCount + 1, _effect));
                                    }
                                }
                            }
                        });
            } else {
                if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                    _action.insertEffect(new PlayoutOptionalBeforeResponsesEffect(_action, _cardTriggersUsed, _playOrder, _passCount + 1, _effect));
                }
            }
        }
    }

    private class PlayoutOptionalAfterResponsesEffect extends UnrespondableEffect {
        private SystemQueueAction _action;
        private PlayOrder _playOrder;
        private int _passCount;
        private Collection<? extends EffectResult> _effectResults;

        private PlayoutOptionalAfterResponsesEffect(SystemQueueAction action, PlayOrder playOrder, int passCount, Collection<? extends EffectResult> effectResults) {
            _action = action;
            _playOrder = playOrder;
            _passCount = passCount;
            _effectResults = effectResults;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            final String activePlayer = _playOrder.getNextPlayer();

            final Map<OptionalTriggerAction, EffectResult> optionalAfterTriggers = _game.getActionsEnvironment().getOptionalAfterTriggers(activePlayer, _effectResults);

            final List<Action> optionalAfterActions = _game.getActionsEnvironment().getOptionalAfterActions(activePlayer, _effectResults);

            List<Action> possibleActions = new LinkedList<Action>(optionalAfterTriggers.keySet());
            possibleActions.addAll(optionalAfterActions);

            if (possibleActions.size() > 0) {
                _game.getUserFeedback().sendAwaitingDecision(activePlayer,
                        new CardActionSelectionDecision(game, 1, "Optional responses", possibleActions) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                Action action = getSelectedAction(result);
                                if (action != null) {
                                    _game.getActionsEnvironment().addActionToStack(action);
                                    if (optionalAfterTriggers.containsKey(action))
                                        optionalAfterTriggers.get(action).optionalTriggerUsed((OptionalTriggerAction) action);
                                    _action.insertEffect(new PlayoutOptionalAfterResponsesEffect(_action, _playOrder, 0, _effectResults));
                                } else {
                                    if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                                        _action.insertEffect(new PlayoutOptionalAfterResponsesEffect(_action, _playOrder, _passCount + 1, _effectResults));
                                    }
                                }
                            }
                        });
            } else {
                if ((_passCount + 1) < _playOrder.getPlayerCount()) {
                    _action.insertEffect(new PlayoutOptionalAfterResponsesEffect(_action, _playOrder, _passCount + 1, _effectResults));
                }
            }
        }
    }

    private class PlayoutAllActionsIfEffectNotCancelledEffect extends UnrespondableEffect {
        private SystemQueueAction _action;
        private List<Action> _actions;

        private PlayoutAllActionsIfEffectNotCancelledEffect(SystemQueueAction action, List<Action> actions) {
            _action = action;
            _actions = actions;
        }

        @Override
        public void doPlayEffect(LotroGame game) {
            if (_actions.size() == 1) {
                _game.getActionsEnvironment().addActionToStack(_actions.get(0));
            } else {
                _game.getUserFeedback().sendAwaitingDecision(_game.getGameState().getCurrentPlayerId(),
                        new ActionSelectionDecision(_game, 1, "Required responses", _actions) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                Action action = getSelectedAction(result);
                                _game.getActionsEnvironment().addActionToStack(action);
                                _actions.remove(action);
                                _action.insertEffect(new PlayoutAllActionsIfEffectNotCancelledEffect(_action, _actions));
                            }
                        });
            }
        }
    }
}
