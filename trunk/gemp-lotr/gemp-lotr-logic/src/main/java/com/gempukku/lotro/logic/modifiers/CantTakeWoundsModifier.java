package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;

public class CantTakeWoundsModifier extends AbstractModifier {
    public CantTakeWoundsModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Can't take wounds", affectFilter, ModifierEffect.WOUND_MODIFIER);
    }

    public CantTakeWoundsModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't take wounds", affectFilter, condition, ModifierEffect.WOUND_MODIFIER);
    }

    @Override
    public boolean canTakeWounds(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, int woundsAlreadyTaken, int woundsToTake) {
        return false;
    }
}
