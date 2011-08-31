package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.TriggeringEffect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.List;
import java.util.Map;

public class AttachPermanentAction extends CostToEffectAction {
    public AttachPermanentAction(final LotroGame game, final PhysicalCard card, Filter filter, final Map<Filter, Integer> attachCostModifiers) {
        super(card, null, "Attach " + card.getBlueprint().getName() + " from hand");

        addCost(new ChooseActiveCardsEffect(card.getOwner(), "Choose target to attach to", 1, 1, filter) {
            @Override
            protected void cardsSelected(List<PhysicalCard> target) {
                addCost(new RemoveCardFromZoneEffect(card));

                int modifier = 0;
                for (Map.Entry<Filter, Integer> filterIntegerEntry : attachCostModifiers.entrySet())
                    if (filterIntegerEntry.getKey().accepts(game.getGameState(), game.getModifiersQuerying(), target.get(0)))
                        modifier += filterIntegerEntry.getValue();

                addCost(new PayPlayOnTwilightCostEffect(card, target.get(0), modifier));

                addEffect(new AttachCardFromHandEffect(card, target.get(0)));
                if (card.getZone() == Zone.DECK)
                    addEffect(new ShuffleDeckEffect(card.getOwner()));
                addEffect(new CardAffectingGameEffect(card));
                addEffect(new TriggeringEffect(new PlayCardResult(card)));

                addFailedCostEffect(new PutCardIntoDiscardEffect(card));
            }
        });
    }
}
