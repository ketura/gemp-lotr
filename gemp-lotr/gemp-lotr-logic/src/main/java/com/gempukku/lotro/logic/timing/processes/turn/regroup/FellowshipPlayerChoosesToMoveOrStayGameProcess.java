package com.gempukku.lotro.logic.timing.processes.turn.regroup;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.RuleUtils;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.ShadowPhasesGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.move.MovementGameProcess;

public class FellowshipPlayerChoosesToMoveOrStayGameProcess implements GameProcess {
    private GameProcess _nextProcess;

    @Override
    public void process(final LotroGame game) {
        final GameState gameState = game.getGameState();
        final String currentPlayerId = gameState.getCurrentPlayerId();
        if (gameState.getMoveCount() < RuleUtils.calculateMoveLimit(game)) {
            if (game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.HAS_TO_MOVE_IF_POSSIBLE)) {
                playerMoves();
            } else {
                game.getUserFeedback().sendAwaitingDecision(currentPlayerId,
                        new MultipleChoiceAwaitingDecision(1, "Do you want to make another move?", new String[]{"Yes", "No"}) {
                            @Override
                            protected void validDecisionMade(int index, String result) {
                                if (result.equals("Yes"))
                                    playerMoves();
                                else {
                                    playerStays(currentPlayerId);
                                }
                            }
                        });
            }
        } else {
            playerStays(currentPlayerId);
        }
    }

    private void playerMoves() {
        _nextProcess = new MovementGameProcess(
                new EndOfPhaseGameProcess(Phase.REGROUP,
                        new ShadowPhasesGameProcess()));
    }

    private void playerStays(String currentPlayerId) {
        _nextProcess = new PlayerReconcilesGameProcess(currentPlayerId,
                new ReturnFollowersToSupportGameProcess(
                        new DiscardAllMinionsGameProcess()));
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
