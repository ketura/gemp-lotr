package com.gempukku.lotro.logic.modifiers.cost;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.timing.PlayConditions;

public class RemoveThreatExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    private final int count;

    public RemoveThreatExtraPlayCostModifier(PhysicalCard source, int count, Condition condition, Filterable ...affects) {
        super(source, "Remove "+count+" threat(s) to play", Filters.and(affects), condition);
        this.count = count;
    }

    @Override
    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
        return PlayConditions.canRemoveThreat(game, card, count);
    }

    @Override
    public void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card) {
        action.appendCost(
                new RemoveThreatsEffect(card, count));
    }
}
