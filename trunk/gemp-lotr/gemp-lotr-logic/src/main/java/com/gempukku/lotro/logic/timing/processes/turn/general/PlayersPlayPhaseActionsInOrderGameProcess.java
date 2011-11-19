package com.gempukku.lotro.logic.timing.processes.turn.general;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.decisions.CardActionSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.List;

public class PlayersPlayPhaseActionsInOrderGameProcess implements GameProcess {
    private LotroGame _game;
    private PlayOrder _playOrder;
    private int _consecutivePasses;
    private GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public PlayersPlayPhaseActionsInOrderGameProcess(LotroGame game, PlayOrder playOrder, int consecutivePasses, GameProcess followingGameProcess) {
        _game = game;
        _playOrder = playOrder;
        _consecutivePasses = consecutivePasses;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(LotroGame game) {
        Skirmish skirmish = _game.getGameState().getSkirmish();
        if (_game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && (_game.getGameState().getSkirmish().isCancelled()
                || skirmish.getFellowshipCharacter() == null || skirmish.getShadowCharacters().size() == 0)) {
            // If the skirmish is cancelled or one side of the skirmish is missing, no more phase actions can be played
            _nextProcess = _followingGameProcess;
        } else {
            String playerId;
            if (_game.getGameState().isConsecutiveAction()) {
                playerId = _playOrder.getLastPlayer();
                _game.getGameState().setConsecutiveAction(false);
            } else {
                playerId = _playOrder.getNextPlayer();
            }

            final List<Action> playableActions = _game.getActionsEnvironment().getPhaseActions(playerId);

            _game.getUserFeedback().sendAwaitingDecision(playerId,
                    new CardActionSelectionDecision(_game, 1, "Choose action to play or Pass", playableActions) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Action action = getSelectedAction(result);
                            if (action != null) {
                                _nextProcess = new PlayersPlayPhaseActionsInOrderGameProcess(_game, _playOrder, 0, _followingGameProcess);
                                _game.getActionsEnvironment().addActionToStack(action);
                            } else {
                                _consecutivePasses++;
                                if (_consecutivePasses >= _playOrder.getPlayerCount())
                                    _nextProcess = _followingGameProcess;
                                else
                                    _nextProcess = new PlayersPlayPhaseActionsInOrderGameProcess(_game, _playOrder, _consecutivePasses, _followingGameProcess);
                            }
                        }
                    });
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
