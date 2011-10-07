package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.PayTwilightCostEffect;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collection;

public class TransferPermanentAction extends ActivateCardAction {
    public TransferPermanentAction(final PhysicalCard card, LotroGame game, Filter filter) {
        super(card, null);

        appendCost(new PayTwilightCostEffect(card));
        appendEffect(
                new ChooseActiveCardsEffect(null, card.getOwner(), "Choose target to attach to", 1, 1, filter,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return modifiersQuerying.canHaveTransferredOn(gameState, card, physicalCard);
                            }
                        }) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> target) {
                        if (target.size() > 0) {
                            appendEffect(new TransferPermanentEffect(card, target.iterator().next()));
                        }
                    }
                });
    }

    @Override
    public String getText(LotroGame game) {
        return "Transfer " + getActionSource().getBlueprint().getName();
    }
}
