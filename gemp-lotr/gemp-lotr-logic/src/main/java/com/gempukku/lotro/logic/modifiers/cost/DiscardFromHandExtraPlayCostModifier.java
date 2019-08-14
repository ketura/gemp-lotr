package com.gempukku.lotro.logic.modifiers.cost;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.timing.PlayConditions;

public class DiscardFromHandExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    private int count;
    private Filterable[] cardFilter;

    public DiscardFromHandExtraPlayCostModifier(PhysicalCard source, Filterable affects, int count, Condition condition, Filterable... cardFilter) {
        super(source, "Discard card(s) from hand to play", affects, condition);
        this.count = count;
        this.cardFilter = cardFilter;
    }

    @Override
    public void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card) {
        action.appendCost(
                new ChooseAndDiscardCardsFromHandEffect(action, card.getOwner(), false, count, cardFilter));
    }

    @Override
    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
        return PlayConditions.canDiscardFromHand(game, card.getOwner(), count, Filters.and(Filters.not(card), Filters.and(cardFilter)));
    }
}
