package com.gempukku.lotro.logic.timing.processes.turn.regroup;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.ShadowPhasesGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.move.MovementGameProcess;

public class FellowshipPlayerChoosesToMoveOrStayGameProcess implements GameProcess {
    private LotroGame _game;

    private GameProcess _nextProcess;

    public FellowshipPlayerChoosesToMoveOrStayGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process() {
        final GameState gameState = _game.getGameState();
        if (gameState.getMoveCount() < _game.getModifiersQuerying().getMoveLimit(gameState, 2)) {
            if (_game.getModifiersQuerying().hasToMoveIfPossible()) {
                _nextProcess = new MovementGameProcess(_game,
                        new EndOfPhaseGameProcess(_game, Phase.REGROUP,
                                new ShadowPhasesGameProcess(_game)));
            } else {
                _game.getUserFeedback().sendAwaitingDecision(gameState.getCurrentPlayerId(),
                        new MultipleChoiceAwaitingDecision(1, "Do you want to make another move?", new String[]{"Yes", "No"}) {
                            @Override
                            protected void validDecisionMade(int index, String result) {
                                if (result.equals("Yes"))
                                    _nextProcess = new MovementGameProcess(_game,
                                            new EndOfPhaseGameProcess(_game, Phase.REGROUP,
                                                    new ShadowPhasesGameProcess(_game)));
                                else {
                                    _nextProcess = new PlayerReconcilesGameProcess(_game, gameState.getCurrentPlayerId(),
                                            new DiscardAllMinionsGameProcess(_game));
                                }
                            }
                        });
            }
        } else {
            _nextProcess = new PlayerReconcilesGameProcess(_game, gameState.getCurrentPlayerId(),
                    new DiscardAllMinionsGameProcess(_game));
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
