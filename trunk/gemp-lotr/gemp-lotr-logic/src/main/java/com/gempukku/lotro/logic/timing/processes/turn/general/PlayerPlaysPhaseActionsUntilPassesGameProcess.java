package com.gempukku.lotro.logic.timing.processes.turn.general;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardActionSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.List;

public class PlayerPlaysPhaseActionsUntilPassesGameProcess implements GameProcess {
    private LotroGame _game;
    private String _playerId;
    private GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public PlayerPlaysPhaseActionsUntilPassesGameProcess(LotroGame game, String playerId, GameProcess followingGameProcess) {
        _game = game;
        _playerId = playerId;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(LotroGame game) {
        final List<Action> playableActions = _game.getActionsEnvironment().getPhaseActions(_playerId);

        _game.getUserFeedback().sendAwaitingDecision(_playerId,
                new CardActionSelectionDecision(_game, 1, "Choose action to play or Pass", playableActions) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        Action action = getSelectedAction(result);
                        if (action != null) {
                            _nextProcess = new PlayerPlaysPhaseActionsUntilPassesGameProcess(_game, _playerId, _followingGameProcess);
                            _game.getActionsEnvironment().addActionToStack(action);
                        } else
                            _nextProcess = _followingGameProcess;
                    }
                });
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
