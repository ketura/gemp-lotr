package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.PayTwilightCostEffect;
import com.gempukku.lotro.cards.effects.PutCardIntoDiscardEffect;
import com.gempukku.lotro.cards.effects.RemoveCardFromHandEffect;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.TriggeringEffect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

public class PlayEventFromHandAction extends CostToEffectAction {
    public PlayEventFromHandAction(PhysicalCard card) {
        super(card, "Play " + card.getBlueprint().getName() + " from hand");

        addCost(new RemoveCardFromHandEffect(card));
        addCost(new PayTwilightCostEffect(card));

        addEffect(new TriggeringEffect(new PlayCardResult(card)));
        addEffect(new PutCardIntoDiscardEffect(card));

        addFailedCostEffect(new PutCardIntoDiscardEffect(card));
    }
}
