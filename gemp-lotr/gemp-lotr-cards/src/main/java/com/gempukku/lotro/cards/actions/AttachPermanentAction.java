package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.TriggeringEffect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Map;

public class AttachPermanentAction extends DefaultCostToEffectAction {
    public AttachPermanentAction(final LotroGame game, final PhysicalCard card, Filter filter, final Map<Filter, Integer> attachCostModifiers) {
        super(card, null, "Attach " + card.getBlueprint().getName() + " from hand");

        addCost(new ChooseActiveCardEffect(card.getOwner(), "Choose target to attach to", filter) {
            @Override
            protected void cardSelected(PhysicalCard target) {
                addCost(new RemoveCardFromZoneEffect(card));

                int modifier = 0;
                for (Map.Entry<Filter, Integer> filterIntegerEntry : attachCostModifiers.entrySet())
                    if (filterIntegerEntry.getKey().accepts(game.getGameState(), game.getModifiersQuerying(), target))
                        modifier += filterIntegerEntry.getValue();

                addCost(new PayPlayOnTwilightCostEffect(card, target, modifier));
                addCost(new AttachCardFromHandEffect(card, target));
                if (card.getZone() == Zone.DECK)
                    addCost(new ShuffleDeckEffect(card.getOwner()));
                addCost(new CardAffectingGameEffect(card));
                addCost(new TriggeringEffect(new PlayCardResult(card, target)));

//                addFailedCostEffect(new PutCardIntoDiscardEffect(card));
            }
        });
    }
}
