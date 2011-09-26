package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

import java.util.Collection;

public class ChooseAndDiscardCardsFromPlayEffect extends ChooseActiveCardsEffect {
    private CostToEffectAction _action;

    public ChooseAndDiscardCardsFromPlayEffect(CostToEffectAction action, String playerId, int minimum, int maximum, Filter... filters) {
        super(playerId, "Choose cards to discard", minimum, maximum, filters);
        _action = action;
    }

    @Override
    public String getText(LotroGame game) {
        return "Choose card(s) to discard from play";
    }

    @Override
    protected void cardsSelected(Collection<PhysicalCard> cards) {
        _action.insertEffect(new DiscardCardsFromPlayEffect(_action.getActionSource(), Filters.in(cards)));
    }
}
