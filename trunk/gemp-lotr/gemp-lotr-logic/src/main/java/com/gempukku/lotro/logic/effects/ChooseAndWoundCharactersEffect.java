package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.SubAction;

import java.util.Collection;

public class ChooseAndWoundCharactersEffect extends ChooseActiveCardsEffect {
    private CostToEffectAction _action;
    private int _count;

    public ChooseAndWoundCharactersEffect(CostToEffectAction action, String playerId, int minimum, int maximum, Filter... filters) {
        this(action, playerId, minimum, maximum, 1, filters);
    }

    public ChooseAndWoundCharactersEffect(CostToEffectAction action, String playerId, int minimum, int maximum, int count, Filter... filters) {
        super(action.getActionSource(), playerId, "Choose characters to wound", minimum, maximum, filters);
        _action = action;
        _count = count;
    }

    @Override
    protected Filter getExtraFilter() {
        return Filters.canTakeWound();
    }

    @Override
    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
        SubAction subAction = new SubAction(_action.getActionSource(), _action.getType());
        for (int i = 0; i < _count; i++)
            subAction.appendEffect(new WoundCharactersEffect(_action.getActionSource(), Filters.in(cards)));
        game.getActionsEnvironment().addActionToStack(subAction);
    }
}
