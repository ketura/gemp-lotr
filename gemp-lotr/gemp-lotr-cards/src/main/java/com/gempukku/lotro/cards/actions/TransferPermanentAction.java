package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.PayTwilightCostEffect;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;

import java.util.Collection;

public class TransferPermanentAction extends ActivateCardAction {
    public TransferPermanentAction(final PhysicalCard card, LotroGame game, Filter filter) {
        super(card, null);

        appendCost(new PayTwilightCostEffect(card));
        appendEffect(new ChooseActiveCardsEffect(card.getOwner(), "Choose target to attach to", 1, 1, filter) {
            @Override
            protected void cardsSelected(Collection<PhysicalCard> target) {
                if (target.size() > 0) {
                    appendEffect(new TransferPermanentEffect(card, target.iterator().next()));
                }
            }
        });
    }
}
