package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;

public abstract class AbstractExtraPlayCostModifier extends AbstractModifier implements ExtraPlayCost {

    public AbstractExtraPlayCostModifier(PhysicalCard source, String text, Filterable affectFilter) {
        super(source, text, affectFilter, ModifierEffect.EXTRA_COST_MODIFIER);
    }

    public AbstractExtraPlayCostModifier(PhysicalCard source, String text, Filterable affectFilter, Condition condition) {
        super(source, text, affectFilter, condition, ModifierEffect.EXTRA_COST_MODIFIER);
    }

    @Override
    public abstract void appendExtraCosts(DefaultGame game, CostToEffectAction action, PhysicalCard card);

    @Override
    public abstract boolean canPayExtraCostsToPlay(DefaultGame game, PhysicalCard card);
}
