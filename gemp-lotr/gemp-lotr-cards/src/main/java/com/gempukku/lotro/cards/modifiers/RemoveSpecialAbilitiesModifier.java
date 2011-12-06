package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

public class RemoveSpecialAbilitiesModifier extends AbstractModifier {
    private Phase _phase;
    private Filterable _affectFilter;

    public RemoveSpecialAbilitiesModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, null, affectFilter);
    }

    public RemoveSpecialAbilitiesModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Removed special abilities", affectFilter, condition, ModifierEffect.ACTION_MODIFIER);
        _affectFilter = affectFilter;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
        if (action.getType() == Action.Type.SPECIAL_ABILITY
                && action.getActionSource() != null && Filters.and(_affectFilter).accepts(gameState, modifiersQuerying, action.getActionSource()))
            return false;
        return true;
    }
}
