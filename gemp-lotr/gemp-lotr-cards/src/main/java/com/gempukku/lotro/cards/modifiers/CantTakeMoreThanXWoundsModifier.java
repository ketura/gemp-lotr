package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.PhaseCondition;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantTakeMoreThanXWoundsModifier extends AbstractModifier {
    private Phase _phase;
    private int _count;

    public CantTakeMoreThanXWoundsModifier(PhysicalCard source, Phase phase, int count, Filterable... affectFilters) {
        this(source, phase, count, null, affectFilters);
    }

    public CantTakeMoreThanXWoundsModifier(PhysicalCard source, final Phase phase, int count, Condition condition, Filterable... affectFilters) {
        super(source, "Can't take more than " + count + " wound(s)", Filters.and(affectFilters),
                new AndCondition(new PhaseCondition(phase), condition), ModifierEffect.WOUND_MODIFIER);
        _phase = phase;
        _count = count;
    }

    @Override
    public boolean canTakeWounds(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, int woundsAlreadyTaken, int woundsToTake) {
        return woundsAlreadyTaken + woundsToTake <= _count;
    }
}
