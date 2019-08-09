package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.AbstractCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.List;

public abstract class AbstractExtraPlayCostModifier extends AbstractModifier {

    public AbstractExtraPlayCostModifier(PhysicalCard source, String text, Filterable affectFilter) {
        super(source, text, affectFilter, ModifierEffect.EXTRA_COST_MODIFIER);
    }

    public AbstractExtraPlayCostModifier(PhysicalCard source, String text, Filterable affectFilter, Condition condition) {
        super(source, text, affectFilter, condition, ModifierEffect.EXTRA_COST_MODIFIER);
    }

    @Override
    public abstract void appendExtraCosts(LotroGame game, AbstractCostToEffectAction action, PhysicalCard card);

    @Override
    public abstract boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card);
}
