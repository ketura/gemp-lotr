package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class AllyParticipatesInArcheryFireModifier extends AbstractModifier {
    public AllyParticipatesInArcheryFireModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Can participate in archery and skirmishes", affectFilter, ModifierEffect.PRESENCE_MODIFIER);
    }

    @Override
    public boolean isAllyParticipateInArcheryFire(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return true;
    }
}