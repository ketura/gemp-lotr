package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;

public class MayNotBearModifier extends AbstractModifier {
    private final Filter _unbearableCardFilter;

    public MayNotBearModifier(LotroPhysicalCard source, Filterable affectFilter, Filterable... unbearableCardFilter) {
        super(source, "Affected by \"may not bear\" limitation", affectFilter, ModifierEffect.TARGET_MODIFIER);
        _unbearableCardFilter = Filters.and(unbearableCardFilter);
    }

    @Override
    public boolean canHavePlayedOn(DefaultGame game, LotroPhysicalCard playedCard, LotroPhysicalCard target) {
        return !_unbearableCardFilter.accepts(game, playedCard);
    }

    @Override
    public boolean canHaveTransferredOn(DefaultGame game, LotroPhysicalCard playedCard, LotroPhysicalCard target) {
        return !_unbearableCardFilter.accepts(game, playedCard);
    }
}
