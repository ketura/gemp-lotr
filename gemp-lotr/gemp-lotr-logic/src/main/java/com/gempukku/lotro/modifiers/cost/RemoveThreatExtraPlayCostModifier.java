package com.gempukku.lotro.modifiers.cost;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.effects.RemoveThreatsEffect;
import com.gempukku.lotro.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.modifiers.Condition;
import com.gempukku.lotro.game.PlayConditions;

public class RemoveThreatExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    private final int count;

    public RemoveThreatExtraPlayCostModifier(LotroPhysicalCard source, int count, Condition condition, Filterable ...affects) {
        super(source, "Remove "+count+" threat(s) to play", Filters.and(affects), condition);
        this.count = count;
    }

    @Override
    public boolean canPayExtraCostsToPlay(DefaultGame game, LotroPhysicalCard card) {
        return PlayConditions.canRemoveThreat(game, card, count);
    }

    @Override
    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card) {
        action.appendCost(
                new RemoveThreatsEffect(card, count));
    }
}
