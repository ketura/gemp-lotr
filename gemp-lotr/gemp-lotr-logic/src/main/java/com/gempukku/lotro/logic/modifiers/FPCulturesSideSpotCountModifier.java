package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

public class FPCulturesSideSpotCountModifier extends AbstractModifier {
    private Side _sidePlayer;
    private int _modifier;

    public FPCulturesSideSpotCountModifier(PhysicalCard source, Side sidePlayer, int modifier) {
        this(source, sidePlayer, null, modifier);
    }

    public FPCulturesSideSpotCountModifier(PhysicalCard source, Side sidePlayer, Condition condition, int modifier) {
        super(source, "Modifies FP culture count", null, condition, ModifierEffect.SPOT_MODIFIER);
        _sidePlayer = sidePlayer;
        _modifier = modifier;
    }

    @Override
    public int getFPCulturesSpotCountModifier(LotroGame game, String playerId) {
        if (GameUtils.isSide(game, _sidePlayer, playerId))
            return _modifier;
        return 0;
    }
}
