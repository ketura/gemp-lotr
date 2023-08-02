package com.gempukku.lotro.effects.choose;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.actions.lotronly.SubAction;
import com.gempukku.lotro.actions.Action;

import java.util.Collection;

public class ChooseAndReturnCardsToHandEffect extends ChooseActiveCardsEffect {
    private final Action _action;

    public ChooseAndReturnCardsToHandEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose cards to return to hand", minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected Filter getExtraFilterForPlaying(DefaultGame game) {
        return new Filter() {
            @Override
            public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                return (_action.getActionSource() == null || game.getModifiersQuerying().canBeReturnedToHand(game, physicalCard, _action.getActionSource()));
            }
        };
    }

    @Override
    protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(new ReturnCardsToHandEffect(_action.getActionSource(), Filters.in(cards)));
        game.getActionsEnvironment().addActionToStack(subAction);
    }
}
