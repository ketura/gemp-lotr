package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;

public class PossessionClassSpotModifier extends AbstractModifier {
    private final PossessionClass _possessionClass;

    public PossessionClassSpotModifier(LotroPhysicalCard source, PossessionClass possessionClass) {
        super(source, "Spotting modifier", null, ModifierEffect.SPOT_MODIFIER);
        _possessionClass = possessionClass;
    }


    @Override
    public int getSpotCountModifier(DefaultGame game, Filterable filter) {
        if (filter == _possessionClass)
            return 1;
        return 0;
    }
}
