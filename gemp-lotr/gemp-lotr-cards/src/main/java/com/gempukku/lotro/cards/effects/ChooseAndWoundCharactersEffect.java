package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collection;

public class ChooseAndWoundCharactersEffect extends ChooseActiveCardsEffect {
    private CostToEffectAction _action;

    public ChooseAndWoundCharactersEffect(CostToEffectAction action, String playerId, int minimum, int maximum, Filter... filters) {
        super(action.getActionSource(), playerId, "Choose characters to wound", minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected Filter getExtraFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canTakeWound(gameState, physicalCard);
            }
        };
    }

    @Override
    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
        SubAction subAction = new SubAction(_action.getActionSource(), _action.getType());
        subAction.appendEffect(new WoundCharactersEffect(_action.getActionSource(), Filters.in(cards)));
        game.getActionsEnvironment().addActionToStack(subAction);
    }
}
