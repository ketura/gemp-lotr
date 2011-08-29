package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.TriggeringEffect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

public class PlayPermanentFromDeckAction extends CostToEffectAction {
    public PlayPermanentFromDeckAction(PhysicalCard card, Zone zone) {
        super(card, "Play " + card.getBlueprint().getName() + " from deck");

        addCost(new RemoveCardFromDeckEffect(card));
        addCost(new PayTwilightCostEffect(card));

        addEffect(new PutCardIntoPlayEffect(card, zone));
        addEffect(new ShuffleDeckEffect(card.getOwner()));
        addEffect(new CardAffectingGameEffect(card));
        addEffect(new TriggeringEffect(new PlayCardResult(card)));

        addFailedCostEffect(new PutCardIntoDiscardEffect(card));
    }
}
