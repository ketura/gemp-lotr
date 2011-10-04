package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndExertCharactersEffect extends ChooseActiveCardsEffect {
    private Action _action;
    private int _count;

    public ChooseAndExertCharactersEffect(Action action, String playerId, int minimum, int maximum, Filter... filters) {
        this(action, playerId, minimum, maximum, 1, filters);
    }

    public ChooseAndExertCharactersEffect(Action action, String playerId, int minimum, int maximum, int count, Filter... filters) {
        super(action.getActionSource(), playerId, "Choose characters to exert", minimum, maximum, filters);
        _action = action;
        _count = count;
    }

    @Override
    protected Filter getExtraFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canBeExerted(gameState, _action.getActionSource(), physicalCard)
                        && modifiersQuerying.getVitality(gameState, physicalCard) > _count;
            }
        };
    }

    @Override
    protected final void cardsSelected(LotroGame game, Collection<PhysicalCard> characters) {
        SubAction subAction = new SubAction(_action.getActionSource(), _action.getType());
        for (int i = 0; i < _count; i++) {
            subAction.appendEffect(new ExertCharactersEffect(_action.getActionSource(), Filters.in(characters)));
        }
        game.getActionsEnvironment().addActionToStack(subAction);

        for (PhysicalCard character : characters)
            forEachCardExertedCallback(character);
    }

    protected void forEachCardExertedCallback(PhysicalCard character) {

    }
}
