package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

public class PlayersCantUseSpecialAbilitiesModifier extends AbstractModifier {

    private Filterable[] _sourceFilters;

    public PlayersCantUseSpecialAbilitiesModifier(PhysicalCard source, Filterable... sourceFilters) {
        this(source, null, sourceFilters);
    }

    public PlayersCantUseSpecialAbilitiesModifier(PhysicalCard source, Condition condition, Filterable... sourceFilters) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _sourceFilters = sourceFilters;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
        if (action.getType() == Action.Type.SPECIAL_ABILITY
                && Filters.and(_sourceFilters).accepts(gameState, modifiersQuerying, action.getActionSource()))
            return false;
        return true;
    }
}
