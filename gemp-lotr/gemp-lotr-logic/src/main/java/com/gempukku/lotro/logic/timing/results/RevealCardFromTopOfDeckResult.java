package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class RevealCardFromTopOfDeckResult extends EffectResult {
    private String _playerId;
    private Collection<PhysicalCard> _revealedCards;

    public RevealCardFromTopOfDeckResult(String playerId, Collection<PhysicalCard> revealedCards) {
        super(Type.REVEAL_CARDS_FROM_TOP_OF_DECK);
        _playerId = playerId;
        _revealedCards = revealedCards;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public Collection<PhysicalCard> getRevealedCards() {
        return _revealedCards;
    }
}
