package com.gempukku.lotro.game.modifiers.cost;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.game.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.game.modifiers.Condition;
import com.gempukku.lotro.game.timing.PlayConditions;

public class ExertExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    private final int count;
    private final Filterable[] exerting;

    public ExertExtraPlayCostModifier(LotroPhysicalCard source, Filterable affects, Condition condition, Filterable ...exerting) {
        this(source, affects, condition, 1, exerting);
    }

    public ExertExtraPlayCostModifier(LotroPhysicalCard source, Filterable affects, Condition condition, int count, Filterable ...exerting) {
        super(source, "Exert to play", Filters.and(affects), condition);
        this.count = count;
        this.exerting = exerting;
    }

    @Override
    public boolean canPayExtraCostsToPlay(DefaultGame game, LotroPhysicalCard card) {
        return PlayConditions.canExert(card, game, 1, count, exerting);
    }

    @Override
    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card) {
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, card.getOwner(), count, count, exerting));
    }
}
