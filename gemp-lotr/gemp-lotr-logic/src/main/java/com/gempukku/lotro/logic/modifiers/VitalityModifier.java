package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

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
    public int getVitalityModifier(LotroGame game, PhysicalCard physicalCard) {
        return _modifier;
    }

    @Override
    public boolean isNonCardTextModifier() {
        return _nonCardTextModifier;
    }
}
