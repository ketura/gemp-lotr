package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.TriggeringEffect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

public class PlayPermanentAction extends CostToEffectAction {
    public PlayPermanentAction(PhysicalCard card, Zone zone) {
        this(card, zone, 0);
    }

    public PlayPermanentAction(PhysicalCard card, Zone zone, int twilightModifier) {
        super(card, null, "Play " + card.getBlueprint().getName());

        addCost(new RemoveCardFromZoneEffect(card));
        addCost(new PayTwilightCostEffect(card, twilightModifier));

        addEffect(new PutCardIntoPlayEffect(card, zone));
        if (card.getZone() == Zone.DECK)
            addEffect(new ShuffleDeckEffect(card.getOwner()));
        addEffect(new CardAffectingGameEffect(card));
        addEffect(new TriggeringEffect(new PlayCardResult(card)));

        addFailedCostEffect(new PutCardIntoDiscardEffect(card));
    }
}
