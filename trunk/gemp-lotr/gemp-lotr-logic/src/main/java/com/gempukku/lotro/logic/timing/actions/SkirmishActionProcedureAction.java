package com.gempukku.lotro.logic.timing.actions;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.decisions.CardActionSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;

public class SkirmishActionProcedureAction extends SystemQueueAction {
    public SkirmishActionProcedureAction() {
        appendEffect(
                new StartPlayingActionsEffect());
    }

    private class StartPlayingActionsEffect extends UnrespondableEffect {
        @Override
        protected void doPlayEffect(LotroGame game) {
            PlayOrder playOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), true);
            appendEffect(
                    new CheckIfSkirmishFinishedEffect(playOrder, 0));
        }
    }

    private class CheckIfSkirmishFinishedEffect extends UnrespondableEffect {
        private PlayOrder _playOrder;
        private int _consecutivePasses;

        private CheckIfSkirmishFinishedEffect(PlayOrder playOrder, int consecutivePasses) {
            _playOrder = playOrder;
            _consecutivePasses = consecutivePasses;
        }

        @Override
        protected void doPlayEffect(LotroGame game) {
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (!game.getGameState().getSkirmish().isCancelled()
                    && skirmish.getFellowshipCharacter() != null && skirmish.getShadowCharacters().size() > 0) {
                appendEffect(
                        new PlayerPlaysNextActionEffect(_playOrder, _consecutivePasses));
            }
        }
    }

    private class PlayerPlaysNextActionEffect extends UnrespondableEffect {
        private PlayOrder _playOrder;
        private int _consecutivePasses;

        private PlayerPlaysNextActionEffect(PlayOrder playOrder, int consecutivePasses) {
            _playOrder = playOrder;
            _consecutivePasses = consecutivePasses;
        }

        @Override
        protected void doPlayEffect(final LotroGame game) {
            String playerId;
            if (game.getGameState().isConsecutiveAction()) {
                playerId = _playOrder.getLastPlayer();
                game.getGameState().setConsecutiveAction(false);
            } else {
                playerId = _playOrder.getNextPlayer();
            }

            final List<Action> playableActions = game.getActionsEnvironment().getPhaseActions(playerId);

            game.getUserFeedback().sendAwaitingDecision(playerId,
                    new CardActionSelectionDecision(game, 1, "Choose action to play or Pass", playableActions, true) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Action action = getSelectedAction(result);
                            if (action != null) {
                                appendEffect(
                                        new CheckIfSkirmishFinishedEffect(_playOrder, 0));
                                game.getActionsEnvironment().addActionToStack(action);
                            } else {
                                if (_consecutivePasses + 1 < _playOrder.getPlayerCount())
                                    appendEffect(
                                            new PlayerPlaysNextActionEffect(_playOrder, _consecutivePasses + 1));
                            }
                        }
                    });
        }
    }
}
