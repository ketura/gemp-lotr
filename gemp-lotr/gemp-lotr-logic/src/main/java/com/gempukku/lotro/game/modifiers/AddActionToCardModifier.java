package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.actions.ActivateCardAction;

import java.util.Collections;
import java.util.List;

public abstract class AddActionToCardModifier extends AbstractModifier {
    public AddActionToCardModifier(PhysicalCard source, Condition condition, Filterable... affectFilter) {
        super(source, "Has extra action from " + GameUtils.getFullName(source), Filters.and(affectFilter), condition, ModifierEffect.EXTRA_ACTION_MODIFIER);
    }

    @Override
    public List<? extends ActivateCardAction> getExtraPhaseAction(DefaultGame game, PhysicalCard card) {
        final ActivateCardAction extraPhaseAction = createExtraPhaseAction(game, card);
        if (extraPhaseAction != null)
            return Collections.singletonList(extraPhaseAction);
        return null;
    }

    protected abstract ActivateCardAction createExtraPhaseAction(DefaultGame game, PhysicalCard matchingCard);
}
