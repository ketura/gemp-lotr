package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class AllyMayNotParticipateInArcheryFireAndSkirmishesModifier extends AbstractModifier {
    private PhysicalCard _source;

    public AllyMayNotParticipateInArcheryFireAndSkirmishesModifier(PhysicalCard source, Condition condition, Filter affectFilter) {
        super(source, "May not participate in archery fire and skirmishes", affectFilter, condition, new ModifierEffect[]{ModifierEffect.PRESENCE_MODIFIER});
        _source = source;
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInArcheryFire(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return true;
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInSkirmishes(GameState gameState, Side sidePlayer, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return true;
    }
}
