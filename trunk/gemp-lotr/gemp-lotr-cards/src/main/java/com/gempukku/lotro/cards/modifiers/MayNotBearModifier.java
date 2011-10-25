package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class MayNotBearModifier extends AbstractModifier {
    private Filter _unbearableCardFilter;

    public MayNotBearModifier(PhysicalCard source, Filter affectFilter, Filter unbearableCardFilter) {
        super(source, "Affected by \"may not bear\" limitation", affectFilter, ModifierEffect.TARGET_MODIFIER);
        _unbearableCardFilter = unbearableCardFilter;
    }

    @Override
    public boolean canHavePlayedOn(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard playedCard, PhysicalCard target) {
        return !_unbearableCardFilter.accepts(gameState, modifiersQuerying, playedCard);
    }

    @Override
    public boolean canHaveTransferredOn(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard playedCard, PhysicalCard target) {
        return !_unbearableCardFilter.accepts(gameState, modifiersQuerying, playedCard);
    }
}
