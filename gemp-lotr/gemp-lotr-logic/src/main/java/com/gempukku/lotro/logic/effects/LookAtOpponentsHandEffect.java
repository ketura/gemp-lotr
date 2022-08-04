package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LookAtOpponentsHandEffect extends AbstractEffect {
    private final String _playerId;
    private final String _opponentId;

    public LookAtOpponentsHandEffect(String playerId, String opponentId) {
        _playerId = playerId;
        _opponentId = opponentId;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getModifiersQuerying().canLookOrRevealCardsInHand(game, _opponentId, _playerId);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (game.getModifiersQuerying().canLookOrRevealCardsInHand(game, _opponentId, _playerId)) {
            List<PhysicalCard> opponentHand = new LinkedList<>(game.getGameState().getHand(_opponentId));

            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Opponent's hand", opponentHand, Collections.emptyList(), 0, 0) {
                        @Override
                        public void decisionMade(String result) {
                        }
                    });
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
