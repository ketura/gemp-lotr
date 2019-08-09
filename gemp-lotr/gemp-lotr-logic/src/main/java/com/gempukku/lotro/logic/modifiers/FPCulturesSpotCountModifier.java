package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class FPCulturesSpotCountModifier extends AbstractModifier {
    private String _playerId;
    private int _modifier;

    public FPCulturesSpotCountModifier(PhysicalCard source, String playerId, int modifier) {
        this(source, playerId, null, modifier);
    }

    public FPCulturesSpotCountModifier(PhysicalCard source, String playerId, Condition condition, int modifier) {
        super(source, "Modifies FP culture count", null, condition, ModifierEffect.SPOT_MODIFIER);
        _playerId = playerId;
        _modifier = modifier;
    }

    @Override
    public int getFPCulturesSpotCountModifier(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId) {
        if (playerId.equals(_playerId))
            return _modifier;
        return 0;
    }
}
