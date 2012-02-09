package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
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
    public abstract List<? extends Effect> getExtraCostsToPlay(GameState gameState, ModifiersQuerying modifiersQueirying, Action action, PhysicalCard card);

    @Override
    public abstract boolean canPayExtraCostsToPlay(GameState gameState, ModifiersQuerying modifiersQueirying, PhysicalCard card);
}
