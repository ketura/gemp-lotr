package com.gempukku.lotro.logic.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.PutCardFromDeckIntoHandEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndPutCardFromDeckIntoHandEffect extends ChooseCardsFromDeckEffect {
    private Action _action;

    public ChooseAndPutCardFromDeckIntoHandEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(playerId, playerId, minimum, maximum, filters);
        _action = action;
    }

    public ChooseAndPutCardFromDeckIntoHandEffect(Action action, String playerId, String deckId, int minimum, int maximum, Filterable... filters) {
        super(playerId, deckId, minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
        if (cards.size() > 0) {
            SubAction subAction = new SubAction(_action);
            for (PhysicalCard card : cards)
                subAction.appendEffect(new PutCardFromDeckIntoHandEffect(card));
            game.getActionsEnvironment().addActionToStack(subAction);
        }
    }
}
