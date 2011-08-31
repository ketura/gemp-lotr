package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class PlayCardResult extends EffectResult {
    private PhysicalCard _playedCard;
    private PhysicalCard _attachedTo;

    public PlayCardResult(PhysicalCard playedCard) {
        this(playedCard, null);
    }

    public PlayCardResult(PhysicalCard playedCard, PhysicalCard attachedTo) {
        super(EffectResult.Type.PLAY);
        _playedCard = playedCard;
        _attachedTo = attachedTo;
    }

    public PhysicalCard getPlayedCard() {
        return _playedCard;
    }

    public PhysicalCard getAttachedTo() {
        return _attachedTo;
    }
}
