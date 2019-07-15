package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.Collections;

public class NegateWoundEffect extends UnrespondableEffect {
    private WoundCharactersEffect _effect;
    private Filter _filter;

    public NegateWoundEffect(WoundCharactersEffect effect, Filterable... filter) {
        _effect =effect;
        _filter = Filters.and(filter);
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        for (PhysicalCard affectedCard : _effect.getAffectedCardsMinusPrevented(game)) {
            if (_filter.accepts(game.getGameState(), game.getModifiersQuerying(), affectedCard))
                _effect.negateWound(game, affectedCard);
        }
    }
}
