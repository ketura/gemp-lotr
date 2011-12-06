package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.BetweenTurnsProcess;

import java.util.HashSet;
import java.util.Set;

public class MulliganProcess implements GameProcess {
    private PlayOrder _playOrder;

    private GameProcess _nextProcess;

    public MulliganProcess(PlayOrder playOrder) {
        _playOrder = playOrder;
    }

    @Override
    public void process(final LotroGame game) {
        final String nextPlayer = _playOrder.getNextPlayer();
        if (nextPlayer != null) {
            game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                    new MultipleChoiceAwaitingDecision(1, "Do you wish to mulligan? (Shuffle cards back and draw 6)", new String[]{"No", "Yes"}) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            if (index == 1) {
                                final GameState gameState = game.getGameState();
                                gameState.sendMessage(nextPlayer + " mulligans");
                                Set<PhysicalCard> hand = new HashSet<PhysicalCard>(gameState.getHand(nextPlayer));
                                gameState.removeCardsFromZone(nextPlayer, hand);
                                for (PhysicalCard card : hand)
                                    gameState.addCardToZone(game, card, Zone.DECK);

                                gameState.shuffleDeck(nextPlayer);
                                for (int i = 0; i < 6; i++)
                                    gameState.playerDrawsCard(nextPlayer);
                            } else {
                                game.getGameState().sendMessage(nextPlayer + " decides not to mulligan");
                            }
                        }
                    });
            _nextProcess = new MulliganProcess(_playOrder);
        } else {
            _nextProcess = new BetweenTurnsProcess();
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
