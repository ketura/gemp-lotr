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
    private int _times;
    private Filterable[] _filters;
    private SubAction _resultSubAction;

    public ChooseAndExertCharactersEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        this(action, playerId, minimum, maximum, 1, filters);
    }

    public ChooseAndExertCharactersEffect(Action action, String playerId, int minimum, int maximum, int times, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose characters to exert", minimum, maximum, filters);
        _action = action;
        _times = times;
        _filters = filters;
    }

    @Override
    protected Filter getExtraFilterForPlaying(LotroGame game) {
        int times = _times;
        do {
            final int exertTimes = times;
            Filter filter = new Filter() {
                @Override
                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                    return modifiersQuerying.canBeExerted(gameState, _action.getActionSource(), physicalCard)
                            && modifiersQuerying.getVitality(gameState, physicalCard) > exertTimes;
                }
            };
            if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.and(_filters, filter)))
                return filter;
            times--;
        } while (times > 0);
        return Filters.none;
    }

    @Override
    protected Filter getExtraFilterForPlayabilityCheck(LotroGame game) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canBeExerted(gameState, _action.getActionSource(), physicalCard)
                        && modifiersQuerying.getVitality(gameState, physicalCard) > _times;
            }
        };
    }

    @Override
    protected final void cardsSelected(LotroGame game, Collection<PhysicalCard> characters) {
        _resultSubAction = new SubAction(_action);
        for (int i = 0; i < _times; i++) {
            _resultSubAction.appendEffect(new ExertCharactersEffect(_action, _action.getActionSource(), characters.toArray(new PhysicalCard[characters.size()])));
        }
        game.getActionsEnvironment().addActionToStack(_resultSubAction);

        for (PhysicalCard character : characters)
            forEachCardExertedCallback(character);
    }

    protected void forEachCardExertedCallback(PhysicalCard character) {

    }

    @Override
    public boolean wasCarriedOut() {
        return super.wasCarriedOut() && _resultSubAction != null && _resultSubAction.wasCarriedOut();
    }
}
