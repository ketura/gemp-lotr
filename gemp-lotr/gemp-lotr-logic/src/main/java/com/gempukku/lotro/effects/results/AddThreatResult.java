package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

public class AddThreatResult extends EffectResult {
    private final LotroPhysicalCard _source;

    public AddThreatResult(LotroPhysicalCard source) {
        super(Type.ADD_THREAT);
        _source = source;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }
}
