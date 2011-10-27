package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

public abstract class AddActionToCardModifier extends AbstractModifier {
    public AddActionToCardModifier(PhysicalCard source, Condition condition, Filterable... affectFilter) {
        super(source, "Has extra action from " + source.getBlueprint().getName(), Filters.and(affectFilter), condition, ModifierEffect.EXTRA_ACTION_MODIFIER);
    }

    @Override
    public List<? extends ActivateCardAction> getExtraPhaseAction(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        final ActivateCardAction extraPhaseAction = createExtraPhaseAction(gameState, modifiersQuerying, card);
        if (extraPhaseAction != null)
            return Collections.singletonList(extraPhaseAction);
        return null;
    }

    protected abstract ActivateCardAction createExtraPhaseAction(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard matchingCard);
}
