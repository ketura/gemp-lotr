package com.gempukku.lotro.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

import java.util.Collection;

public abstract class ChooseActiveCardEffect extends ChooseActiveCardsEffect {
    public ChooseActiveCardEffect(LotroPhysicalCard source, String playerId, String choiceText, Filterable... filters) {
        super(source, playerId, choiceText, 1, 1, filters);
    }

    protected abstract void cardSelected(DefaultGame game, LotroPhysicalCard card);

    @Override
    protected final void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        if (cards.size() == 1)
            cardSelected(game, cards.iterator().next());
    }
}
