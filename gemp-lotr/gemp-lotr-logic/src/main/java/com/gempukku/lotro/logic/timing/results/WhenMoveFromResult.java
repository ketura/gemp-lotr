package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class WhenMoveFromResult extends EffectResult {
    private PhysicalCard _site;

    public WhenMoveFromResult(PhysicalCard site) {
        super(EffectResult.Type.WHEN_MOVE_FROM);
        _site = site;
    }

    public PhysicalCard getSite() {
        return _site;
    }
}
