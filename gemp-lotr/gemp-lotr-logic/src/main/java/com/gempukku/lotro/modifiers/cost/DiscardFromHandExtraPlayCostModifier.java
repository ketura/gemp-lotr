package com.gempukku.lotro.modifiers.cost;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.modifiers.condition.Condition;
import com.gempukku.lotro.game.PlayConditions;

public class DiscardFromHandExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    private final int count;
    private final Filterable[] cardFilter;

    public DiscardFromHandExtraPlayCostModifier(LotroPhysicalCard source, Filterable affects, int count, Condition condition, Filterable... cardFilter) {
        super(source, "Discard card(s) from hand to play", affects, condition);
        this.count = count;
        this.cardFilter = cardFilter;
    }

    @Override
    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card) {
        action.appendCost(
                new ChooseAndDiscardCardsFromHandEffect(action, card.getOwner(), false, count, cardFilter));
    }

    @Override
    public boolean canPayExtraCostsToPlay(DefaultGame game, LotroPhysicalCard card) {
        return PlayConditions.canDiscardFromHand(game, card.getOwner(), count, Filters.and(Filters.not(card), Filters.and(cardFilter)));
    }
}
