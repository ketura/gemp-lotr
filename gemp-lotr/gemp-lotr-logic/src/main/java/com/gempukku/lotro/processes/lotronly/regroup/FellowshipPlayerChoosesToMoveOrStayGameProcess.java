package com.gempukku.lotro.processes.lotronly.regroup;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.processes.lotronly.MovementGameProcess;
import com.gempukku.lotro.processes.lotronly.ShadowPhasesGameProcess;
import com.gempukku.lotro.processes.turn.EndOfPhaseGameProcess;
import com.gempukku.lotro.processes.turn.EndOfTurnGameProcess;
import com.gempukku.lotro.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.effects.TriggeringResultEffect;
import com.gempukku.lotro.modifiers.ModifierFlag;
import com.gempukku.lotro.rules.RuleUtils;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.effects.results.FreePlayerMoveDecisionResult;

public class FellowshipPlayerChoosesToMoveOrStayGameProcess implements GameProcess {
    private GameProcess _nextProcess;

    @Override
    public void process(final DefaultGame game) {
        final GameState gameState = game.getGameState();
        final String currentPlayerId = gameState.getCurrentPlayerId();
        if (gameState.getMoveCount() < RuleUtils.calculateMoveLimit(game)) {
            if (game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.HAS_TO_MOVE_IF_POSSIBLE)) {
                playerMoves(game);
            } else {
                game.getUserFeedback().sendAwaitingDecision(currentPlayerId,
                        new MultipleChoiceAwaitingDecision(1, "Do you want to make another move?", new String[]{"Yes", "No"}) {
                            @Override
                            protected void validDecisionMade(int index, String result) {
                                if (result.equals("Yes"))
                                    playerMoves(game);
                                else {
                                    playerStays(game);
                                }
                            }
                        });
            }
        } else {
            playerStays(game);
        }
    }

    private void playerMoves(DefaultGame game) {

        game.getGameState().sendMessage(game.getGameState().getCurrentPlayerId() + " decides to move again.");

        _nextProcess = new MovementGameProcess(
                new EndOfPhaseGameProcess(Phase.REGROUP,
                        new ShadowPhasesGameProcess()));

        final SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new TriggeringResultEffect(new FreePlayerMoveDecisionResult(true), "Free Peoples player decides to move again."));
        game.getActionsEnvironment().addActionToStack(action);
    }

    private void playerStays(DefaultGame game) {
        game.getGameState().sendMessage(game.getGameState().getCurrentPlayerId() + " decides to stay and reconcile.");

        _nextProcess = game.getFormat().getAdventure().getPlayerStaysGameProcess(game,
                new EndOfPhaseGameProcess(Phase.REGROUP,
                        new EndOfTurnGameProcess()));

        final SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new TriggeringResultEffect(new FreePlayerMoveDecisionResult(false), "Free Peoples player decides to stay."));
        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
