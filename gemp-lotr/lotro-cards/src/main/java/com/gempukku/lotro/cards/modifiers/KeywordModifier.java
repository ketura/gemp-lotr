package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class KeywordModifier extends AbstractModifier {
    private Keyword _keyword;

    public KeywordModifier(PhysicalCard physicalCard, String text, Filter affectFilter, Keyword keyword) {
        super(physicalCard, text, affectFilter);
        _keyword = keyword;
    }

    @Override
    public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
        return (result || keyword == _keyword);
    }

    @Override
    public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result) {
        if (keyword == _keyword)
            return result + 1;
        else
            return result;
    }
}
