package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.effects.EffectResult;

public class PlayCardResult extends EffectResult {
    private final Zone _playedFrom;
    private final LotroPhysicalCard _playedCard;
    private final LotroPhysicalCard _attachedTo;
    private final LotroPhysicalCard _attachedOrStackedPlayedFrom;

    public PlayCardResult(Zone playedFrom, LotroPhysicalCard playedCard) {
        super(EffectResult.Type.PLAY);
        _playedFrom = playedFrom;
        _playedCard = playedCard;
        _attachedTo = null;
        _attachedOrStackedPlayedFrom = null;
    }

    public PlayCardResult(Zone playedFrom, LotroPhysicalCard playedCard, LotroPhysicalCard attachedTo, LotroPhysicalCard attachedOrStackedPlayedFrom) {
        super(EffectResult.Type.PLAY);
        _playedFrom = playedFrom;
        _playedCard = playedCard;
        _attachedTo = attachedTo;
        _attachedOrStackedPlayedFrom = attachedOrStackedPlayedFrom;
    }

    public LotroPhysicalCard getPlayedCard() {
        return _playedCard;
    }

    public LotroPhysicalCard getAttachedTo() {
        return _attachedTo;
    }

    public LotroPhysicalCard getAttachedOrStackedPlayedFrom() {
        return _attachedOrStackedPlayedFrom;
    }

    public Zone getPlayedFrom() {
        return _playedFrom;
    }
}
