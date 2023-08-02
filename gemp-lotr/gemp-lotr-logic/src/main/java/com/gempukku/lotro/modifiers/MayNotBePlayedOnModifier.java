package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.DefaultGame;

public class MayNotBePlayedOnModifier extends AbstractModifier {
    private final Filter _unplayableCardFilter;

    public MayNotBePlayedOnModifier(LotroPhysicalCard source, Filter affectFilter, Filter unplayableCardFilter) {
        super(source, "Affected by \"may not be played on\" limitation", affectFilter, ModifierEffect.TARGET_MODIFIER);
        _unplayableCardFilter = unplayableCardFilter;
    }

    @Override
    public boolean canHavePlayedOn(DefaultGame game, LotroPhysicalCard playedCard, LotroPhysicalCard target) {
        return !_unplayableCardFilter.accepts(game, playedCard);
    }
}
