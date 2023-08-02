package com.gempukku.lotro.modifiers.cost;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.effects.AddBurdenEffect;
import com.gempukku.lotro.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.modifiers.condition.Condition;
import com.gempukku.lotro.game.PlayConditions;

public class AddBurdenExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    private final int count;

    public AddBurdenExtraPlayCostModifier(LotroPhysicalCard source, int count, Condition condition, Filterable ...affects) {
        super(source, "Add "+count+" burden(s) to play", Filters.and(affects), condition);
        this.count = count;
    }

    @Override
    public boolean canPayExtraCostsToPlay(DefaultGame game, LotroPhysicalCard card) {
        return PlayConditions.canAddBurdens(game, card.getOwner(), card);
    }

    @Override
    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card) {
        action.appendCost(
                new AddBurdenEffect(card.getOwner(), card, count));
    }
}
