package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class MayNotBePlayedOnModifier extends AbstractModifier {
    private Filter _unplayableCardFilter;

    public MayNotBePlayedOnModifier(PhysicalCard source, Filter affectFilter, Filter unplayableCardFilter) {
        super(source, "Affected by \"may not be played\" limitation", affectFilter, ModifierEffect.TARGET_MODIFIER);
        _unplayableCardFilter = unplayableCardFilter;
    }

    @Override
    public boolean canHavePlayedOn(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard playedCard, PhysicalCard target) {
        return !_unplayableCardFilter.accepts(gameState, modifiersQuerying, playedCard);
    }
}
