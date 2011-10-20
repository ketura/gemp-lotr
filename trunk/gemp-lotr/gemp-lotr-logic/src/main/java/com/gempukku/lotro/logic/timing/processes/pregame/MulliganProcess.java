package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.StartOfTurnGameProcess;

import java.util.HashSet;
import java.util.Set;

public class MulliganProcess implements GameProcess {
    private LotroGame _game;
    private PlayOrder _playOrder;

    private GameProcess _nextProcess;

    public MulliganProcess(LotroGame game, PlayOrder playOrder) {
        _game = game;
        _playOrder = playOrder;
    }

    @Override
    public void process() {
        final String nextPlayer = _playOrder.getNextPlayer();
        if (nextPlayer != null) {
            _game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                    new MultipleChoiceAwaitingDecision(1, "Do you wish to mulligan? (Shuffle cards back and draw 6)", new String[]{"No", "Yes"}) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            if (result.equals("Yes")) {
                                final GameState gameState = _game.getGameState();
                                Set<PhysicalCard> hand = new HashSet<PhysicalCard>(gameState.getHand(nextPlayer));
                                gameState.removeCardsFromZone(hand);
                                for (PhysicalCard card : hand)
                                    gameState.addCardToZone(_game, card, Zone.DECK);

                                gameState.shuffleDeck(nextPlayer);
                                for (int i = 0; i < 6; i++)
                                    gameState.playerDrawsCard(nextPlayer);
                            }
                        }
                    });
            _nextProcess = new MulliganProcess(_game, _playOrder);
        } else {
            _nextProcess = new StartOfTurnGameProcess(_game);
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
