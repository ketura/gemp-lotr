package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class MayNotBearModifier extends AbstractModifier {
    private final Filter _unbearableCardFilter;

    public MayNotBearModifier(PhysicalCard source, Filterable affectFilter, Filterable... unbearableCardFilter) {
        super(source, "Affected by \"may not bear\" limitation", affectFilter, ModifierEffect.TARGET_MODIFIER);
        _unbearableCardFilter = Filters.and(unbearableCardFilter);
    }

    @Override
    public boolean canHavePlayedOn(DefaultGame game, PhysicalCard playedCard, PhysicalCard target) {
        return !_unbearableCardFilter.accepts(game, playedCard);
    }

    @Override
    public boolean canHaveTransferredOn(DefaultGame game, PhysicalCard playedCard, PhysicalCard target) {
        return !_unbearableCardFilter.accepts(game, playedCard);
    }
}
