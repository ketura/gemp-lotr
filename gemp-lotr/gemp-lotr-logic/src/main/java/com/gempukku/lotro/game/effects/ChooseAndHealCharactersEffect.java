package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;

public class ChooseAndHealCharactersEffect extends ChooseActiveCardsEffect {
    private final Action _action;
    private final String _playerId;
    private final int _count;

    public ChooseAndHealCharactersEffect(Action action, String playerId, Filterable... filters) {
        this(action, playerId, 1, 1, filters);
    }

    public ChooseAndHealCharactersEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        this(action, playerId, minimum, maximum, 1, filters);
    }

    public ChooseAndHealCharactersEffect(Action action, String playerId, int minimum, int maximum, int count, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose characters to heal", minimum, maximum, filters);
        _action = action;
        _playerId = playerId;
        _count = count;
    }

    @Override
    protected Filter getExtraFilterForPlayabilityCheck(DefaultGame game) {
        return Filters.and(
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                        return game.getGameState().getWounds(physicalCard) >= _count && game.getModifiersQuerying().canBeHealed(game, physicalCard);
                    }
                });
    }

    @Override
    protected Filter getExtraFilterForPlaying(DefaultGame game) {
        return Filters.and(
                Filters.wounded,
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                        return game.getModifiersQuerying().canBeHealed(game, physicalCard);
                    }
                });
    }

    @Override
    protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        SubAction subAction = new SubAction(_action);
        for (int i = 0; i < _count; i++)
            subAction.appendEffect(new HealCharactersEffect(_action.getActionSource(), _action.getPerformingPlayer(), Filters.in(cards)));
        game.getActionsEnvironment().addActionToStack(subAction);

        for (LotroPhysicalCard character : cards)
            forEachCardChosenToHealCallback(character);
    }

    protected void forEachCardChosenToHealCallback(LotroPhysicalCard character) {

    }
}
