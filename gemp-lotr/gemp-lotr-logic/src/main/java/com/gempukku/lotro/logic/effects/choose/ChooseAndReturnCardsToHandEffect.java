package com.gempukku.lotro.logic.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndReturnCardsToHandEffect extends ChooseActiveCardsEffect {
    private Action _action;

    public ChooseAndReturnCardsToHandEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose cards to return to hand", minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected Filter getExtraFilterForPlaying(LotroGame game) {
        return new Filter() {
            @Override
            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                return (_action.getActionSource() == null || game.getModifiersQuerying().canBeReturnedToHand(game, physicalCard, _action.getActionSource()));
            }
        };
    }

    @Override
    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
        SubCostToEffectAction subAction = new SubCostToEffectAction(_action);
        subAction.appendEffect(new ReturnCardsToHandEffect(_action.getActionSource(), Filters.in(cards)));
        game.getActionsEnvironment().addActionToStack(subAction);
    }
}
