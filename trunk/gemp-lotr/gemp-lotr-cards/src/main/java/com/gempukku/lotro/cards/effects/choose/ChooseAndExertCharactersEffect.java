package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Filterable;
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
    private SubAction _resultSubAction;

    public ChooseAndExertCharactersEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        this(action, playerId, minimum, maximum, 1, filters);
    }

    public ChooseAndExertCharactersEffect(Action action, String playerId, int minimum, int maximum, int count, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose characters to exert", minimum, maximum, filters);
        _action = action;
        _count = count;
    }

    @Override
    protected Filter getExtraFilter(LotroGame game) {
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
        _resultSubAction = new SubAction(_action);
        for (int i = 0; i < _count; i++) {
            _resultSubAction.appendEffect(new ExertCharactersEffect(_action.getActionSource(), Filters.in(characters)));
        }
        game.getActionsEnvironment().addActionToStack(_resultSubAction);

        for (PhysicalCard character : characters)
            forEachCardExertedCallback(character);
    }

    protected void forEachCardExertedCallback(PhysicalCard character) {

    }

    @Override
    public boolean wasSuccessful() {
        return _resultSubAction != null && _resultSubAction.wasSuccessful();
    }

    @Override
    public boolean wasCarriedOut() {
        return _resultSubAction != null && _resultSubAction.wasCarriedOut();
    }
}
