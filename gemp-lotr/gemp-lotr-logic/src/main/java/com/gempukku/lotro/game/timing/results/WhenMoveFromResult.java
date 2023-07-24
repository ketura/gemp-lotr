package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class WhenMoveFromResult extends EffectResult {
    private final LotroPhysicalCard _site;

    public WhenMoveFromResult(LotroPhysicalCard site) {
        super(EffectResult.Type.WHEN_MOVE_FROM);
        _site = site;
    }

    public LotroPhysicalCard getSite() {
        return _site;
    }
}
