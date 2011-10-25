package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ResistanceModifier extends AbstractModifier {
    private int _modifier;

    public ResistanceModifier(PhysicalCard source, Filter affectFilter, int modifier) {
        super(source, "Resistance " + ((modifier < 0) ? modifier : ("+" + modifier)), affectFilter, ModifierEffect.RESISTANCE_MODIFIER);
        _modifier = modifier;
    }

    @Override
    public int getResistanceModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return _modifier;
    }
}
