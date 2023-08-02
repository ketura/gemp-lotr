package com.gempukku.lotro.effects.choose;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.SubAction;
import com.gempukku.lotro.effects.PutCardFromDeckIntoHandEffect;
import com.gempukku.lotro.actions.Action;

import java.util.Collection;

public class ChooseAndPutCardFromDeckIntoHandEffect extends ChooseCardsFromDeckEffect {
    private final Action _action;

    public ChooseAndPutCardFromDeckIntoHandEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(playerId, playerId, minimum, maximum, filters);
        _action = action;
    }

    public ChooseAndPutCardFromDeckIntoHandEffect(Action action, String playerId, String deckId, int minimum, int maximum, Filterable... filters) {
        super(playerId, deckId, minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        if (cards.size() > 0) {
            SubAction subAction = new SubAction(_action);
            for (LotroPhysicalCard card : cards)
                subAction.appendEffect(new PutCardFromDeckIntoHandEffect(card));
            game.getActionsEnvironment().addActionToStack(subAction);
        }
    }
}
