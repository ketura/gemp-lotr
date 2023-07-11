package com.gempukku.lotro.game.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.game.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.game.actions.SubAction;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;

public class ChooseAndExhaustCharactersEffect extends ChooseActiveCardsEffect {
    private final Action _action;

    public ChooseAndExhaustCharactersEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose characters to exert", minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected Filter getExtraFilterForPlaying(DefaultGame game) {
        return new Filter() {
            @Override
            public boolean accepts(DefaultGame game, PhysicalCard physicalCard) {
                return game.getModifiersQuerying().canBeExerted(game, _action.getActionSource(), physicalCard)
                        && game.getModifiersQuerying().getVitality(game, physicalCard) > 1;
            }
        };
    }

    @Override
    protected final void cardsSelected(DefaultGame game, Collection<PhysicalCard> characters) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(new ExhaustCharacterEffect(_action.getActionSource(), subAction, Filters.in(characters)));
        game.getActionsEnvironment().addActionToStack(subAction);
    }
}
