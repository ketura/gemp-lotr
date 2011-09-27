package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LookAtOpponentsHandEffect implements Effect {
    private String _playerId;
    private String _opponentId;

    public LookAtOpponentsHandEffect(String playerId, String opponentId) {
        _playerId = playerId;
        _opponentId = opponentId;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        if (game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), _opponentId)) {
            List<PhysicalCard> opponentHand = new LinkedList<PhysicalCard>(game.getGameState().getHand(_opponentId));

            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Opponent's hand", opponentHand, Collections.<PhysicalCard>emptyList(), 0, 0) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                        }
                    });
        }
        return null;
    }
}
