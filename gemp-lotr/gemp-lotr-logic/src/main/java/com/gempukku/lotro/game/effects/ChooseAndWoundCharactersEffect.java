package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;

public class ChooseAndWoundCharactersEffect extends ChooseActiveCardsEffect {
    private final Action _action;
    private final int _count;

    private String _sourceText;

    public ChooseAndWoundCharactersEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        this(action, playerId, minimum, maximum, 1, filters);
    }

    public ChooseAndWoundCharactersEffect(Action action, String playerId, int minimum, int maximum, int count, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose characters to wound", minimum, maximum, filters);
        _action = action;
        _count = count;
    }

    public void setSourceText(String sourceText) {
        _sourceText = sourceText;
    }

    @Override
    protected Filter getExtraFilterForPlaying(DefaultGame game) {
        return Filters.canTakeWounds(_action.getActionSource(), 1);
    }

    @Override
    protected Filter getExtraFilterForPlayabilityCheck(DefaultGame game) {
        return Filters.canTakeWounds(_action.getActionSource(), _count);
    }

    @Override
    protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        SubAction subAction = new SubAction(_action);
        for (int i = 0; i < _count; i++) {
            LotroPhysicalCard source = (_action.getActionSource() != null) ? _action.getActionSource() : null;
            WoundCharactersEffect woundEffect = new WoundCharactersEffect(source, cards.toArray(new LotroPhysicalCard[cards.size()]));
            if (_sourceText != null)
                woundEffect.setSourceText(_sourceText);
            subAction.appendEffect(woundEffect);
        }
        game.getActionsEnvironment().addActionToStack(subAction);
        woundedCardsCallback(cards);
    }

    protected void woundedCardsCallback(Collection<LotroPhysicalCard> cards) {

    }
}
