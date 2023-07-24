package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class RevealCardFromHandResult extends EffectResult {
    private final LotroPhysicalCard _source;
    private final String _playerId;
    private final LotroPhysicalCard _card;

    public RevealCardFromHandResult(LotroPhysicalCard source, String playerId, LotroPhysicalCard card) {
        super(Type.FOR_EACH_REVEALED_FROM_HAND);
        _source = source;
        _playerId = playerId;
        _card = card;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public LotroPhysicalCard getRevealedCard() {
        return _card;
    }
}
