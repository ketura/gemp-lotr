package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndHealCharactersEffect extends ChooseActiveCardsEffect {
    private Action _action;
    private String _playerId;
    private int _count;

    public ChooseAndHealCharactersEffect(Action action, String playerId, Filter... filters) {
        this(action, playerId, 1, 1, filters);
    }

    public ChooseAndHealCharactersEffect(Action action, String playerId, int minimum, int maximum, Filter... filters) {
        this(action, playerId, minimum, maximum, 1, filters);
    }

    public ChooseAndHealCharactersEffect(Action action, String playerId, int minimum, int maximum, int count, Filter... filters) {
        super(action.getActionSource(), playerId, "Choose character(s) to heal", minimum, maximum, filters);
        _action = action;
        _playerId = playerId;
        _count = count;
    }

    @Override
    protected Filter getExtraFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canBeHealed(gameState, physicalCard);
            }
        };
    }

    @Override
    public String getText(LotroGame game) {
        return "Choose character(s) to heal";
    }

    @Override
    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
        SubAction subAction = new SubAction(_action);
        for (int i = 0; i < _count; i++)
            subAction.appendEffect(new HealCharactersEffect(_playerId, Filters.in(cards)));
        game.getActionsEnvironment().addActionToStack(subAction);
    }
}
