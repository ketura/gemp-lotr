package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class VitalityModifier extends AbstractModifier {
    private int _modifier;
    private boolean _nonCardTextModifier;

    public VitalityModifier(PhysicalCard source, Filterable affectFilter, int modifier) {
        this(source, affectFilter, modifier, false);
    }

    public VitalityModifier(PhysicalCard source, Filterable affectFilter, int modifier, boolean nonCardTextModifier) {
        super(source, "Vitality " + ((modifier < 0) ? modifier : ("+" + modifier)), affectFilter, ModifierEffect.VITALITY_MODIFIER);
        _modifier = modifier;
        _nonCardTextModifier = nonCardTextModifier;
    }

    @Override
    public int getVitalityModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return _modifier;
    }

    @Override
    public boolean isNonCardTextModifier() {
        return _nonCardTextModifier;
    }
}
