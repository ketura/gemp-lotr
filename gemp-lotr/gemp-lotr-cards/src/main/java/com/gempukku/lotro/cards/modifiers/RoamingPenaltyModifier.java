package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class RoamingPenaltyModifier extends AbstractModifier {
    private int _modifier;

    public RoamingPenaltyModifier(PhysicalCard source, Filterable affectFilter, int modifier) {
        super(source, "Roaming penalty " + ((modifier > 0) ? ("+" + modifier) : modifier), affectFilter, ModifierEffect.TWILIGHT_COST_MODIFIER);
        _modifier = modifier;
    }

    public RoamingPenaltyModifier(PhysicalCard source, Filterable affectFilter, Condition condition, int modifier) {
        super(source, "Roaming penalty " + ((modifier > 0) ? ("+" + modifier) : modifier), affectFilter, condition, ModifierEffect.TWILIGHT_COST_MODIFIER);
        _modifier = modifier;
    }

    @Override
    public int getRoamingPenaltyModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return _modifier;
    }
}
