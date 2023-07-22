package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class PlayCardResult extends EffectResult {
    private final Zone _playedFrom;
    private final PhysicalCard _playedCard;
    private final PhysicalCard _attachedTo;
    private final PhysicalCard _attachedOrStackedPlayedFrom;

    public PlayCardResult(Zone playedFrom, PhysicalCard playedCard) {
        super(EffectResult.Type.PLAY);
        _playedFrom = playedFrom;
        _playedCard = playedCard;
        _attachedTo = null;
        _attachedOrStackedPlayedFrom = null;
    }

    public PlayCardResult(Zone playedFrom, PhysicalCard playedCard, PhysicalCard attachedTo, PhysicalCard attachedOrStackedPlayedFrom) {
        super(EffectResult.Type.PLAY);
        _playedFrom = playedFrom;
        _playedCard = playedCard;
        _attachedTo = attachedTo;
        _attachedOrStackedPlayedFrom = attachedOrStackedPlayedFrom;
    }

    public PhysicalCard getPlayedCard() {
        return _playedCard;
    }

    public PhysicalCard getAttachedTo() {
        return _attachedTo;
    }

    public PhysicalCard getAttachedOrStackedPlayedFrom() {
        return _attachedOrStackedPlayedFrom;
    }

    public Zone getPlayedFrom() {
        return _playedFrom;
    }
}
