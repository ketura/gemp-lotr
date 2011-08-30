package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.PayTwilightCostEffect;
import com.gempukku.lotro.cards.effects.PutCardIntoDiscardEffect;
import com.gempukku.lotro.cards.effects.RemoveCardFromZoneEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.TriggeringEffect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

public class PlayEventAction extends CostToEffectAction {
    public PlayEventAction(PhysicalCard card) {
        super(card, "Play " + card.getBlueprint().getName() + " from hand");

        addCost(new RemoveCardFromZoneEffect(card));
        addCost(new PayTwilightCostEffect(card));

        if (card.getZone() == Zone.DECK)
            addEffect(new ShuffleDeckEffect(card.getOwner()));
        addEffect(new TriggeringEffect(new PlayCardResult(card)));
        addEffect(new PutCardIntoDiscardEffect(card));

        addFailedCostEffect(new PutCardIntoDiscardEffect(card));
    }
}
