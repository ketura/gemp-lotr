package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.cards.modifiers.spotting.SimpleLotroCardBlueprint;
import com.gempukku.lotro.cards.modifiers.spotting.SimplePhysicalCard;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class KeywordSpotModifier extends AbstractModifier {
    private Keyword _keyword;

    public KeywordSpotModifier(PhysicalCard source, Keyword keyword) {
        super(source, "Spotting modifier", null, new ModifierEffect[]{ModifierEffect.SPOT_MODIFIER});
        _keyword = keyword;
    }

    @Override
    public int getSpotCountModifier(GameState gameState, ModifiersQuerying modifiersQuerying, Filter filter) {
        if (filter.accepts(gameState, modifiersQuerying,
                new SimplePhysicalCard(
                        new SimpleLotroCardBlueprint() {
                            @Override
                            public boolean hasKeyword(Keyword keyword) {
                                return keyword == _keyword;
                            }
                        })))
            return +1;
        return 0;
    }
}
