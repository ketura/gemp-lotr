package com.gempukku.lotro.game.timing.processes.turn.tribbles;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.actions.tribbles.TribblesPlayPermanentAction;
import com.gempukku.lotro.game.decisions.CardActionSelectionDecision;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.timing.processes.DefaultGameProcess;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.TribblesGame;

import java.util.LinkedList;
import java.util.List;

public class TribblesPlayerDrawsAndCanPlayProcess extends DefaultGameProcess<TribblesGame> {
    private final String _playerId;
    public TribblesPlayerDrawsAndCanPlayProcess(String playerId) {
        _playerId = playerId;
    }

    @Override
    public void process(final TribblesGame game) {
        if (game.getGameState().getDeck(_playerId).size() == 0) {
            game.getGameState().sendMessage(_playerId + " can't draw a card");
            game.getGameState().setPlayerDecked(_playerId, true);
        } else {
            game.getGameState().playerDrawsCard(_playerId);
            game.getGameState().sendMessage(_playerId + " drew a card");
            List<? extends LotroPhysicalCard> playerHand = game.getGameState().getHand(_playerId);
            LotroPhysicalCard cardDrawn = playerHand.get(playerHand.size() - 1);
            final List<Action> playableActions = new LinkedList<>();
            if (game.checkPlayRequirements(cardDrawn)) {
                TribblesPlayPermanentAction action = new TribblesPlayPermanentAction(cardDrawn, Zone.PLAY_PILE);
                playableActions.add(action);
            }

            if (playableActions.size() == 0 && game.shouldAutoPass(_playerId, game.getGameState().getCurrentPhase())) {
                playerPassed(game);
            } else {
                game.getUserFeedback().sendAwaitingDecision(_playerId,
                        new CardActionSelectionDecision(game, 1, "Play card that was just drawn or Pass", playableActions) {
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
    }

    private void playerPassed(DefaultGame game) {
        game.getGameState().playerPassEffect();
    }

    @Override
    public GameProcess getNextProcess() {
        return new TribblesEndOfTurnGameProcess();
    }
}
