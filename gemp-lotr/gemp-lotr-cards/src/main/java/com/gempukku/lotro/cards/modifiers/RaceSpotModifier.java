package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class RaceSpotModifier extends AbstractModifier {
    private Race _race;

    public RaceSpotModifier(PhysicalCard source, Race race) {
        super(source, "Spotting modifier", null, ModifierEffect.SPOT_MODIFIER);
        _race = race;
    }


    @Override
    public int getSpotCountModifier(GameState gameState, ModifiersQuerying modifiersQuerying, Filterable filter) {
        if (filter == _race)
            return 1;
        return 0;
    }
}