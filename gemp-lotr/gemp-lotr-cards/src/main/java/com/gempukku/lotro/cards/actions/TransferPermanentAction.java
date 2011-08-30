package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.PayTwilightCostEffect;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

public class TransferPermanentAction extends CostToEffectAction {
    public TransferPermanentAction(final PhysicalCard card, LotroGame game, Filter filter) {
        super(card, "Transfer " + card.getBlueprint().getName());

        addCost(new ChooseActiveCardEffect(card.getOwner(), "Choose target to attach to", filter) {
            @Override
            protected void cardSelected(LotroGame game, PhysicalCard target) {
                addCost(new PayTwilightCostEffect(card));
                addCost(new TransferPermanentEffect(card, target));
            }
        });
    }
}
