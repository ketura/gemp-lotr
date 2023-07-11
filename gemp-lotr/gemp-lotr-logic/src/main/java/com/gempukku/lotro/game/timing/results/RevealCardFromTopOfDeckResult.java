package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.timing.EffectResult;

public class RevealCardFromTopOfDeckResult extends EffectResult {
    private final String _playerId;
    private final PhysicalCard _revealedCard;

    public RevealCardFromTopOfDeckResult(String playerId, PhysicalCard revealedCard) {
        super(Type.FOR_EACH_REVEALED_FROM_TOP_OF_DECK);
        _playerId = playerId;
        _revealedCard = revealedCard;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public PhysicalCard getRevealedCard() {
        return _revealedCard;
    }
}
