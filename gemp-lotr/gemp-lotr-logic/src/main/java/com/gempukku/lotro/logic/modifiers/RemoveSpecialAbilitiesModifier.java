package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

public class RemoveSpecialAbilitiesModifier extends AbstractModifier {
    private Filter _affectFilter;

    public RemoveSpecialAbilitiesModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, null, affectFilter);
    }

    public RemoveSpecialAbilitiesModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Removed special abilities", affectFilter, condition, ModifierEffect.ACTION_MODIFIER);
        _affectFilter = Filters.and(affectFilter);
    }

    @Override
    public boolean canPlayAction(LotroGame game, String performingPlayer, Action action) {
        if (action.getType() == Action.Type.SPECIAL_ABILITY
                && action.getActionSource() != null && _affectFilter.accepts(game, action.getActionSource()))
            return false;
        return true;
    }
}
