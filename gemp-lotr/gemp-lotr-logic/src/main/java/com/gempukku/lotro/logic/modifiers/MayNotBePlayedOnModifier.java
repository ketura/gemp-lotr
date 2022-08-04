package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class MayNotBePlayedOnModifier extends AbstractModifier {
    private final Filter _unplayableCardFilter;

    public MayNotBePlayedOnModifier(PhysicalCard source, Filter affectFilter, Filter unplayableCardFilter) {
        super(source, "Affected by \"may not be played on\" limitation", affectFilter, ModifierEffect.TARGET_MODIFIER);
        _unplayableCardFilter = unplayableCardFilter;
    }

    @Override
    public boolean canHavePlayedOn(LotroGame game, PhysicalCard playedCard, PhysicalCard target) {
        return !_unplayableCardFilter.accepts(game, playedCard);
    }
}
