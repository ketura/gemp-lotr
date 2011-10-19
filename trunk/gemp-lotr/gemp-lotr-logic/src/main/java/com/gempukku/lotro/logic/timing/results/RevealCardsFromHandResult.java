package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class RevealCardsFromHandResult extends EffectResult {
    private PhysicalCard _source;
    private String _playerId;
    private Collection<PhysicalCard> _revealedCards;

    public RevealCardsFromHandResult(PhysicalCard source, String playerId, Collection<PhysicalCard> revealedCards) {
        super(Type.REVEAL_CARDS_FROM_HAND);
        _source = source;
        _playerId = playerId;
        _revealedCards = revealedCards;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public Collection<PhysicalCard> getRevealedCards() {
        return _revealedCards;
    }
}
