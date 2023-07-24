package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

import java.util.Collection;

public class WoundResult extends EffectResult {
    private final Collection<LotroPhysicalCard> _sources;
    private final LotroPhysicalCard _card;

    public WoundResult(Collection<LotroPhysicalCard> sources, LotroPhysicalCard card) {
        super(EffectResult.Type.FOR_EACH_WOUNDED);
        _sources = sources;
        _card = card;
    }

    public Collection<LotroPhysicalCard> getSources() {
        return _sources;
    }

    public LotroPhysicalCard getWoundedCard() {
        return _card;
    }
}
