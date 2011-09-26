package com.gempukku.lotro.cards.costs;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;

import java.util.Collection;

public class ChooseAndDiscardCardsFromPlayCost extends ChooseActiveCardsCost {
    private CostToEffectAction _action;

    public ChooseAndDiscardCardsFromPlayCost(CostToEffectAction action, String playerId, int minimum, int maximum, Filter... filters) {
        super(playerId, "Choose cards to discard", minimum, maximum, filters);
        _action = action;
    }

    @Override
    public String getText(LotroGame game) {
        return "Choose card to discard from play";
    }

    @Override
    protected void cardsSelected(Collection<PhysicalCard> cards, boolean success) {
        _action.insertCost(new DiscardCardsFromPlayCost(cards.toArray(new PhysicalCard[cards.size()])));
        if (!success)
            _action.appendCost(new FailCost());
    }
}
