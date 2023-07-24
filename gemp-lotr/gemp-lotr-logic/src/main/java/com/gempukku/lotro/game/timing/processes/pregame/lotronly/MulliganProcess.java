package com.gempukku.lotro.game.timing.processes.pregame.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.processes.turn.BetweenTurnsProcess;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.game.timing.processes.GameProcess;

import java.util.HashSet;
import java.util.Set;

public class MulliganProcess implements GameProcess {
    private final PlayOrder _playOrder;

    private GameProcess _nextProcess;

    public MulliganProcess(PlayOrder playOrder) {
        _playOrder = playOrder;
    }

    @Override
    public void process(final DefaultGame game) {
        final int handSize = game.getFormat().getHandSize();

        final String nextPlayer = _playOrder.getNextPlayer();
        if (nextPlayer != null) {
            game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                    new MultipleChoiceAwaitingDecision(1, "Do you wish to mulligan? (Shuffle cards back and draw " + (handSize - 2) + ")", new String[]{"No", "Yes"}) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            if (index == 1) {
                                final GameState gameState = game.getGameState();
                                gameState.sendMessage(nextPlayer + " mulligans");
                                Set<LotroPhysicalCard> hand = new HashSet<>(gameState.getHand(nextPlayer));
                                gameState.removeCardsFromZone(nextPlayer, hand);
                                for (LotroPhysicalCard card : hand)
                                    gameState.addCardToZone(game, card, Zone.DECK);

                                gameState.shuffleDeck(nextPlayer);
                                for (int i = 0; i < handSize - 2; i++)
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
