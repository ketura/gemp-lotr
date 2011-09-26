package com.gempukku.lotro.cards.costs;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;

import java.util.Collection;

public class ChooseAndExertCharactersCost extends ChooseActiveCardsCost {
    private CostToEffectAction _action;
    private String _playerId;

    public ChooseAndExertCharactersCost(CostToEffectAction action, String playerId, int minimum, int maximum, Filter... filters) {
        super(playerId, "Choose characters to exert", minimum, maximum, Filters.and(filters, Filters.canExert()));
        _action = action;
        _playerId = playerId;
    }

    @Override
    protected void cardsSelected(Collection<PhysicalCard> characters, boolean success) {
        _action.insertCost(new ExertCharactersCost(_playerId, characters.toArray(new PhysicalCard[characters.size()])));
        if (!success)
            _action.appendCost(new FailCost());
    }
}
