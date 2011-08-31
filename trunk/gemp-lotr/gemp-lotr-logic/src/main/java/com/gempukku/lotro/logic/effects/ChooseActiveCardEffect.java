package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;

import java.util.List;

public abstract class ChooseActiveCardEffect extends ChooseActiveCardsEffect {
    public ChooseActiveCardEffect(String playerId, String choiceText, Filter... filters) {
        super(playerId, choiceText, 1, 1, filters);
    }

    protected abstract void cardSelected(PhysicalCard card);

    @Override
    protected final void cardsSelected(List<PhysicalCard> cards) {
        cardSelected(cards.get(0));
    }
}
