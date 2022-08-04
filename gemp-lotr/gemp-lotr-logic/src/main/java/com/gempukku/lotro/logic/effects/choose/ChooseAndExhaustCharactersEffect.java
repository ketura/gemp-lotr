package com.gempukku.lotro.logic.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndExhaustCharactersEffect extends ChooseActiveCardsEffect {
    private final Action _action;

    public ChooseAndExhaustCharactersEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose characters to exert", minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected Filter getExtraFilterForPlaying(LotroGame game) {
        return new Filter() {
            @Override
            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                return game.getModifiersQuerying().canBeExerted(game, _action.getActionSource(), physicalCard)
                        && game.getModifiersQuerying().getVitality(game, physicalCard) > 1;
            }
        };
    }

    @Override
    protected final void cardsSelected(LotroGame game, Collection<PhysicalCard> characters) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(new ExhaustCharacterEffect(_action.getActionSource(), subAction, Filters.in(characters)));
        game.getActionsEnvironment().addActionToStack(subAction);
    }
}
