package com.gempukku.lotro.game.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.decisions.CardActionSelectionDecision;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.timing.UnrespondableEffect;

import java.util.List;

public class SkirmishActionProcedureAction extends SystemQueueAction {
    public SkirmishActionProcedureAction() {
        appendEffect(
                new StartPlayingActionsEffect());
    }

    private class StartPlayingActionsEffect extends UnrespondableEffect {
        @Override
        protected void doPlayEffect(DefaultGame game) {
            PlayOrder playOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), true);
            appendEffect(
                    new CheckIfSkirmishFinishedEffect(playOrder, 0));
        }
    }

    private class CheckIfSkirmishFinishedEffect extends UnrespondableEffect {
        private final PlayOrder _playOrder;
        private final int _consecutivePasses;

        private CheckIfSkirmishFinishedEffect(PlayOrder playOrder, int consecutivePasses) {
            _playOrder = playOrder;
            _consecutivePasses = consecutivePasses;
        }

        @Override
        protected void doPlayEffect(DefaultGame game) {
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (!game.getGameState().getSkirmish().isCancelled()
                    && skirmish.getFellowshipCharacter() != null && skirmish.getShadowCharacters().size() > 0) {
                appendEffect(
                        new PlayerPlaysNextActionEffect(_playOrder, _consecutivePasses));
            }
        }
    }

    private class PlayerPlaysNextActionEffect extends UnrespondableEffect {
        private final PlayOrder _playOrder;
        private final int _consecutivePasses;

        private PlayerPlaysNextActionEffect(PlayOrder playOrder, int consecutivePasses) {
            _playOrder = playOrder;
            _consecutivePasses = consecutivePasses;
        }

        @Override
        protected void doPlayEffect(final DefaultGame game) {
            String playerId;
            if (game.getGameState().isConsecutiveAction()) {
                playerId = _playOrder.getLastPlayer();
                game.getGameState().setConsecutiveAction(false);
            } else {
                playerId = _playOrder.getNextPlayer();
            }

            final List<Action> playableActions = game.getActionsEnvironment().getPhaseActions(playerId);

            if (playableActions.size() == 0 && game.shouldAutoPass(playerId, Phase.SKIRMISH)) {
                playerPassed();
            } else {
                game.getUserFeedback().sendAwaitingDecision(playerId,
                        new CardActionSelectionDecision(game, 1, "Choose action to play or Pass", playableActions) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                Action action = getSelectedAction(result);
                                if (action != null) {
                                    appendEffect(
                                            new CheckIfSkirmishFinishedEffect(_playOrder, 0));
                                    game.getActionsEnvironment().addActionToStack(action);
                                } else {
                                    playerPassed();
                                }
                            }
                        });
            }
        }

        private void playerPassed() {
            if (_consecutivePasses + 1 < _playOrder.getPlayerCount())
                appendEffect(
                        new PlayerPlaysNextActionEffect(_playOrder, _consecutivePasses + 1));
        }
    }
}
