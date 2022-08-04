package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class FPCulturesSpotCountModifier extends AbstractModifier {
    private final String _playerId;
    private final int _modifier;

    public FPCulturesSpotCountModifier(PhysicalCard source, String playerId, int modifier) {
        this(source, playerId, null, modifier);
    }

    public FPCulturesSpotCountModifier(PhysicalCard source, String playerId, Condition condition, int modifier) {
        super(source, "Modifies FP culture count", null, condition, ModifierEffect.SPOT_MODIFIER);
        _playerId = playerId;
        _modifier = modifier;
    }

    @Override
    public int getFPCulturesSpotCountModifier(LotroGame game, String playerId) {
        if (playerId.equals(_playerId))
            return _modifier;
        return 0;
    }
}
