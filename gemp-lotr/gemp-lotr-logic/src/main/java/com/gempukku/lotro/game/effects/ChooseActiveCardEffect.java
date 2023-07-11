package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

import java.util.Collection;

public abstract class ChooseActiveCardEffect extends ChooseActiveCardsEffect {
    public ChooseActiveCardEffect(PhysicalCard source, String playerId, String choiceText, Filterable... filters) {
        super(source, playerId, choiceText, 1, 1, filters);
    }

    protected abstract void cardSelected(DefaultGame game, PhysicalCard card);

    @Override
    protected final void cardsSelected(DefaultGame game, Collection<PhysicalCard> cards) {
        if (cards.size() == 1)
            cardSelected(game, cards.iterator().next());
    }
}
