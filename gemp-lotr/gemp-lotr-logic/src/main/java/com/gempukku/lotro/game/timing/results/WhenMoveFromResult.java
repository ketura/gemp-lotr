package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.timing.EffectResult;

public class WhenMoveFromResult extends EffectResult {
    private final PhysicalCard _site;

    public WhenMoveFromResult(PhysicalCard site) {
        super(EffectResult.Type.WHEN_MOVE_FROM);
        _site = site;
    }

    public PhysicalCard getSite() {
        return _site;
    }
}
