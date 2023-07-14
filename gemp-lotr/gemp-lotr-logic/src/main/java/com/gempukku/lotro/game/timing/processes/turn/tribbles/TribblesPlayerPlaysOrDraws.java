package com.gempukku.lotro.game.timing.processes.turn.tribbles;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.decisions.CardActionSelectionDecision;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.timing.processes.GameProcess;

import java.util.List;

public class TribblesPlayerPlaysOrDraws implements GameProcess {
    private final String _playerId;
    private final GameProcess _followingGameProcess;
    private GameProcess _nextProcess;

    public TribblesPlayerPlaysOrDraws(String playerId, GameProcess followingGameProcess) {
        _playerId = playerId;
        _followingGameProcess = followingGameProcess;
        _nextProcess = followingGameProcess;
    }

    @Override
    public void process(final DefaultGame game) {
        game.getGameState().sendMessage("DEBUG: Beginning TribblesPlayerPlaysOrDraws");
        final List<Action> playableActions = game.getActionsEnvironment().getPhaseActions(_playerId);

        if (playableActions.size() == 0 && game.shouldAutoPass(_playerId, game.getGameState().getCurrentPhase())) {
            playerPassed(game);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardActionSelectionDecision(game, 1, "Play " + game.getGameState().getCurrentPhase().getHumanReadable() + " action or Pass", playableActions) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Action action = getSelectedAction(result);
                            if (action != null) {
//                                _nextProcess = new TribblesPlayerPlaysOrDraws(_playerId, _followingGameProcess);
                                game.getActionsEnvironment().addActionToStack(action);
                            } else
                                playerPassed(game);
                        }
                    });
        }
    }

    private void playerPassed(DefaultGame game) {
        game.getGameState().playerDrawsCard(_playerId);
        game.getGameState().playerPassEffect();
        _nextProcess = _followingGameProcess;
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
