package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.TriggeringEffect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

public class PlayPermanentFromHandAction extends CostToEffectAction {
    public PlayPermanentFromHandAction(PhysicalCard card, Zone zone) {
        this(card, zone, 0);
    }

    public PlayPermanentFromHandAction(PhysicalCard card, Zone zone, int twilightModifier) {
        super(card, "Play " + card.getBlueprint().getName() + " from hand");

        addCost(new RemoveCardFromHandEffect(card));
        addCost(new PayTwilightCostEffect(card, twilightModifier));

        addEffect(new PutCardIntoPlayEffect(card, zone));
        addEffect(new CardAffectingGameEffect(card));
        addEffect(new TriggeringEffect(new PlayCardResult(card)));

        addFailedCostEffect(new PutCardIntoDiscardEffect(card));
    }
}
