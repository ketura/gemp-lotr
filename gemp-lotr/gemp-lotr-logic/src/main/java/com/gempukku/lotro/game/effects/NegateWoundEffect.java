package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.UnrespondableEffect;

public class NegateWoundEffect extends UnrespondableEffect {
    private final WoundCharactersEffect _effect;
    private final Filter _filter;

    public NegateWoundEffect(WoundCharactersEffect effect, Filterable... filter) {
        _effect =effect;
        _filter = Filters.and(filter);
    }

    @Override
    protected void doPlayEffect(DefaultGame game) {
        for (PhysicalCard affectedCard : _effect.getAffectedCardsMinusPrevented(game)) {
            if (_filter.accepts(game, affectedCard))
                _effect.negateWound(game, affectedCard);
        }
    }
}
