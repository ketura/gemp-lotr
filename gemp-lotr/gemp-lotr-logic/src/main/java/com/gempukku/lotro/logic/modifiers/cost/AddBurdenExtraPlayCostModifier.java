package com.gempukku.lotro.logic.modifiers.cost;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.timing.PlayConditions;

public class AddBurdenExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    private int count;

    public AddBurdenExtraPlayCostModifier(PhysicalCard source, int count, Condition condition, Filterable ...affects) {
        super(source, "Add "+count+" burden(s) to play", Filters.and(affects), condition);
        this.count = count;
    }

    @Override
    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
        return PlayConditions.canAddBurdens(game, card.getOwner(), card);
    }

    @Override
    public void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card) {
        action.appendCost(
                new AddBurdenEffect(card.getOwner(), card, count));
    }
}
