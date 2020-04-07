package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PreventCardEffect extends UnrespondableEffect {
    private PreventableCardEffect _effect;
    private Filter _filter;

    public PreventCardEffect(PreventableCardEffect effect, Filterable... filters) {
        _effect = effect;
        _filter = Filters.and(filters);
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        for (PhysicalCard affectedCard : _effect.getAffectedCardsMinusPrevented(game)) {
            if (_filter.accepts(game, affectedCard))
                _effect.preventEffect(game, affectedCard);
        }
    }
}
