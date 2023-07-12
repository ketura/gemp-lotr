package com.gempukku.lotro.game.modifiers.cost;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.effects.AddBurdenEffect;
import com.gempukku.lotro.game.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.game.modifiers.Condition;
import com.gempukku.lotro.game.timing.PlayConditions;

public class AddBurdenExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    private final int count;

    public AddBurdenExtraPlayCostModifier(PhysicalCard source, int count, Condition condition, Filterable ...affects) {
        super(source, "Add "+count+" burden(s) to play", Filters.and(affects), condition);
        this.count = count;
    }

    @Override
    public boolean canPayExtraCostsToPlay(DefaultGame game, PhysicalCard card) {
        return PlayConditions.canAddBurdens(game, card.getOwner(), card);
    }

    @Override
    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, PhysicalCard card) {
        action.appendCost(
                new AddBurdenEffect(card.getOwner(), card, count));
    }
}
