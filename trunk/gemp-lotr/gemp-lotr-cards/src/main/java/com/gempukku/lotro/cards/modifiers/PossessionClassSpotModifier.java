package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.cards.modifiers.spotting.SimpleLotroCardBlueprint;
import com.gempukku.lotro.cards.modifiers.spotting.SimplePhysicalCard;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class PossessionClassSpotModifier extends AbstractModifier {
    private PossessionClass _possessionClass;

    public PossessionClassSpotModifier(PhysicalCard source, PossessionClass possessionClass) {
        super(source, "Spotting modifier", null, ModifierEffect.SPOT_MODIFIER);
        _possessionClass = possessionClass;
    }

    @Override
    public int getSpotCountModifier(GameState gameState, ModifiersQuerying modifiersQuerying, Filter filter) {
        if (filter.accepts(gameState, modifiersQuerying,
                new SimplePhysicalCard(
                        new SimpleLotroCardBlueprint() {
                            @Override
                            public PossessionClass getPossessionClass() {
                                return _possessionClass;
                            }
                        })))
            return 1;
        return 0;
    }
}
