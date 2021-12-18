package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CantCancelSkirmishModifier extends AbstractModifier {
    private Filter _cardFilter;


    public CantCancelSkirmishModifier(PhysicalCard source, String text, Condition condition, Filterable affectFilter) {
        super(source, text, affectFilter, condition, ModifierEffect.DISCARD_FROM_PLAY_MODIFIER);
        _cardFilter = Filters.and(affectFilter);
    }

    @Override
    public boolean canCancelSkirmish(LotroGame game, PhysicalCard card) {
        if (_cardFilter.accepts(game, card))
            return false;
        return true;
    }
}
