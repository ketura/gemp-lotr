package com.gempukku.lotro.cards.costs;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collection;

public class ChooseAndExertCharactersCost extends ChooseActiveCardsCost {
    private CostToEffectAction _action;

    public ChooseAndExertCharactersCost(CostToEffectAction action, String playerId, int minimum, int maximum, Filter... filters) {
        super(playerId, "Choose characters to exert", minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected void cardsSelected(Collection<PhysicalCard> characters, boolean success) {
        _action.insertCost(new ExertCharactersCost(_action.getActionSource(), characters.toArray(new PhysicalCard[characters.size()])));
        if (!success)
            _action.appendCost(new FailCost());
    }

    @Override
    protected Filter getExtraFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canBeExerted(gameState, _action.getActionSource(), physicalCard)
                        && modifiersQuerying.getVitality(gameState, physicalCard) > 1;
            }
        };
    }
}
