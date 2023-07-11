package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.timing.EffectResult;

import java.util.Collection;

public class WoundResult extends EffectResult {
    private final Collection<PhysicalCard> _sources;
    private final PhysicalCard _card;

    public WoundResult(Collection<PhysicalCard> sources, PhysicalCard card) {
        super(EffectResult.Type.FOR_EACH_WOUNDED);
        _sources = sources;
        _card = card;
    }

    public Collection<PhysicalCard> getSources() {
        return _sources;
    }

    public PhysicalCard getWoundedCard() {
        return _card;
    }
}
