package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CancelStrengthBonusTargetModifier extends AbstractModifier {
    private Filterable _sourceFilter;

    public CancelStrengthBonusTargetModifier(PhysicalCard source, Filterable affectFilter, Filterable sourceFilter) {
        super(source, "Has some strength bonuses cancelled", affectFilter, ModifierEffect.STRENGTH_BONUS_TARGET_MODIFIER);
        _sourceFilter = sourceFilter;
    }

    public CancelStrengthBonusTargetModifier(PhysicalCard source, Condition condition, Filterable affectFilter, Filterable sourceFilter) {
        super(source, "Has some strength bonuses cancelled", affectFilter, condition, ModifierEffect.STRENGTH_BONUS_TARGET_MODIFIER);
        _sourceFilter = sourceFilter;
    }

    @Override
    public boolean appliesStrengthBonusModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, PhysicalCard modifierTaget) {
        if (_sourceFilter == null || (modifierSource != null && Filters.and(_sourceFilter).accepts(gameState, modifiersQuerying, modifierSource)))
            return false;
        return true;
    }
}
