package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class RevealCardFromTopOfDeckResult extends EffectResult {
    private String _playerId;
    private PhysicalCard _revealedCard;

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
