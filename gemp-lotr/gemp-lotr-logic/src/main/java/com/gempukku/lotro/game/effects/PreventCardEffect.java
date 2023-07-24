package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;

public class PreventCardEffect extends UnrespondableEffect {
    private final PreventableCardEffect _effect;
    private final Filter _filter;

    public PreventCardEffect(PreventableCardEffect effect, Filterable... filters) {
        _effect = effect;
        _filter = Filters.and(filters);
    }

    @Override
    protected void doPlayEffect(DefaultGame game) {
        for (LotroPhysicalCard affectedCard : _effect.getAffectedCardsMinusPrevented(game)) {
            if (_filter.accepts(game, affectedCard))
                _effect.preventEffect(game, affectedCard);
        }
    }
}
