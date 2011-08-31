package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;

public abstract class ChooseArbitraryCardEffect extends UnrespondableEffect {
    private String _playerId;
    private String _choiceText;
    private List<PhysicalCard> _cards;

    public ChooseArbitraryCardEffect(String playerId, String choiceText, List<PhysicalCard> cards) {
        _playerId = playerId;
        _choiceText = choiceText;
        _cards = cards;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return (_cards.size() > 0);
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new ArbitraryCardsSelectionDecision(1, _choiceText, _cards, 1, 1) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        PhysicalCard selectedCard = getSelectedCardsByResponse(result).get(0);
                        cardSelected(selectedCard);
                    }
                });
    }

    protected abstract void cardSelected(PhysicalCard card);
}
