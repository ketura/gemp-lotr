package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.modifiers.condition.Condition;

public abstract class AbstractExtraPlayCostModifier extends AbstractModifier implements ExtraPlayCost {

    public AbstractExtraPlayCostModifier(LotroPhysicalCard source, String text, Filterable affectFilter) {
        super(source, text, affectFilter, ModifierEffect.EXTRA_COST_MODIFIER);
    }

    public AbstractExtraPlayCostModifier(LotroPhysicalCard source, String text, Filterable affectFilter, Condition condition) {
        super(source, text, affectFilter, condition, ModifierEffect.EXTRA_COST_MODIFIER);
    }

    @Override
    public abstract void appendExtraCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card);

    @Override
    public abstract boolean canPayExtraCostsToPlay(DefaultGame game, LotroPhysicalCard card);
}
