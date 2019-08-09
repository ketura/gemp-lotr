package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class PossessionClassSpotModifier extends AbstractModifier {
    private PossessionClass _possessionClass;

    public PossessionClassSpotModifier(PhysicalCard source, PossessionClass possessionClass) {
        super(source, "Spotting modifier", null, ModifierEffect.SPOT_MODIFIER);
        _possessionClass = possessionClass;
    }


    @Override
    public int getSpotCountModifier(LotroGame game, Filterable filter) {
        if (filter == _possessionClass)
            return 1;
        return 0;
    }
}
