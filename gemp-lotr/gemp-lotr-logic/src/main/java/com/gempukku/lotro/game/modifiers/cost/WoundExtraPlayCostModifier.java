package com.gempukku.lotro.game.modifiers.cost;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.CostToEffectAction;
import com.gempukku.lotro.game.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.game.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.game.modifiers.Condition;
import com.gempukku.lotro.game.timing.PlayConditions;

public class WoundExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    private final int count;
    private final Filterable[] wounding;

    public WoundExtraPlayCostModifier(PhysicalCard source, Filterable affects, Condition condition, Filterable... wounding) {
        this(source, affects, condition, 1, wounding);
    }

    public WoundExtraPlayCostModifier(PhysicalCard source, Filterable affects, Condition condition, int count, Filterable... wounding) {
        super(source, "Wound to play", Filters.and(affects), condition);
        this.count = count;
        this.wounding = wounding;
    }

    @Override
    public boolean canPayExtraCostsToPlay(DefaultGame game, PhysicalCard card) {
        return PlayConditions.canWound(card, game, 1, count, wounding);
    }

    @Override
    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, PhysicalCard card) {
        action.appendCost(
                new ChooseAndWoundCharactersEffect(action, card.getOwner(), count, count, wounding));
    }
}
