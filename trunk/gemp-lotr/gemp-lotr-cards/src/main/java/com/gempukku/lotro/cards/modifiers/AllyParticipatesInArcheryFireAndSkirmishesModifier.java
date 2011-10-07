package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class AllyParticipatesInArcheryFireAndSkirmishesModifier extends AbstractModifier {
    public AllyParticipatesInArcheryFireAndSkirmishesModifier(PhysicalCard source, Filter affectFilter) {
        super(source, "Can participate in archery and skirmishes", affectFilter, new ModifierEffect[]{ModifierEffect.PRESENCE_MODIFIER});
    }

    @Override
    public boolean isAllyParticipateInArcheryFire(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return true;
    }

    @Override
    public boolean isAllyParticipateInSkirmishes(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return true;
    }
}
