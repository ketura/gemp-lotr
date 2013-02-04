package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collection;

public class CantWoundWithCardModifier extends AbstractModifier {
    private Filterable _preventWoundWithFilter;

    public CantWoundWithCardModifier(PhysicalCard source, Filterable affectFilter, Filterable preventWoundWithFilter) {
        this(source, affectFilter, null, preventWoundWithFilter);
    }

    public CantWoundWithCardModifier(PhysicalCard source, Filterable affectFilter, Condition condition, Filterable preventWoundWithFilter) {
        super(source, "Affected by wound preventing effect", affectFilter, condition, ModifierEffect.WOUND_MODIFIER);
        _preventWoundWithFilter = preventWoundWithFilter;
    }

    @Override
    public boolean canTakeWounds(GameState gameState, ModifiersQuerying modifiersQuerying, Collection<PhysicalCard> woundSources, PhysicalCard physicalCard, int woundsAlreadyTaken, int woundsToTake) {
        for (PhysicalCard woundSource : woundSources) {
            if (Filters.and(_preventWoundWithFilter).accepts(gameState, modifiersQuerying, woundSource))
                return false;
        }

        return true;
    }
}
