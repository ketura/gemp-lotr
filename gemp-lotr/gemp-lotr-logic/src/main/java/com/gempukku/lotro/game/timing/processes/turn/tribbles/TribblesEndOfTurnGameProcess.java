package com.gempukku.lotro.game.timing.processes.turn.tribbles;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.TribblesGame;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.game.effects.AbstractSuccessfulEffect;
import com.gempukku.lotro.game.effects.TriggeringResultEffect;
import com.gempukku.lotro.game.modifiers.ModifiersLogic;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.DefaultGameProcess;
import com.gempukku.lotro.game.timing.results.EndOfTurnResult;

public class TribblesEndOfTurnGameProcess extends DefaultGameProcess<TribblesGame> {
    private GameProcess _nextProcess;
    @Override
    public void process(TribblesGame game) {
//        game.getGameState().sendMessage("DEBUG: Beginning TribblesEndOfTurnGameProcess");
        SystemQueueAction action = new SystemQueueAction();
        action.setText("End of turn");
        action.appendEffect(
                new TriggeringResultEffect(null, new EndOfTurnResult(), "End of turn"));
        action.appendEffect(
                new AbstractSuccessfulEffect() {
                    @Override
                    public String getText(DefaultGame game) {
                        return null;
                    }

                    @Override
                    public Type getType() {
                        return null;
                    }

                    @Override
                    public void playEffect(DefaultGame game) {
                        ((ModifiersLogic) game.getModifiersEnvironment()).signalEndOfTurn();
                        ((DefaultActionsEnvironment) game.getActionsEnvironment()).signalEndOfTurn();
                        game.getGameState().stopAffectingCardsForCurrentPlayer();
                    }
                });
        boolean playerWentOut = false;
        for (String playerId : game.getPlayers()) {
            if (game.getGameState().getHand(playerId).size() == 0) {
                playerWentOut = true;
            }
        }
        if (playerWentOut) {
            _nextProcess = new TribblesEndOfRoundGameProcess();
        } else {
            _nextProcess = new TribblesBetweenTurnsProcess();
        }
        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
