package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class PlayCardResult extends EffectResult {
    private PhysicalCard _playedCard;

    public PlayCardResult(PhysicalCard playedCard) {
        super(EffectResult.Type.PLAY);
        _playedCard = playedCard;
    }

    public PhysicalCard getPlayedCard() {
        return _playedCard;
    }
}
