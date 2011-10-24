package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class OverwhelmedByMultiplierModifier extends AbstractModifier {
    private int _multiplier;

    public OverwhelmedByMultiplierModifier(PhysicalCard source, Filter affectFilter, int multiplier) {
        this(source, affectFilter, null, multiplier);
    }

    public OverwhelmedByMultiplierModifier(PhysicalCard source, Filter affectFilter, Condition condition, int multiplier) {
        super(source, "Cannot be overwhelmed unless his strength is *" + multiplier, affectFilter, condition, new ModifierEffect[]{ModifierEffect.OVERWHELM_MODIFIER});
        _multiplier = multiplier;
    }

    @Override
    public boolean isOverwhelmedByStrength(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, int strength, int opposingStrength) {
        return (opposingStrength >= strength * _multiplier);
    }

}
