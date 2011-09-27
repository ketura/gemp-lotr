package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;

public class KeywordModifier extends AbstractModifier {
    private Keyword _keyword;
    private int _count;

    public KeywordModifier(PhysicalCard physicalCard, Filter affectFilter, Keyword keyword) {
        this(physicalCard, affectFilter, keyword, 1);
    }

    public KeywordModifier(PhysicalCard physicalCard, Filter affectFilter, Keyword keyword, int count) {
        super(physicalCard, "Has " + keyword.getHumanReadable() + ((count > 1) ? (" +" + count) : ""), affectFilter, new ModifierEffect[]{ModifierEffect.KEYWORD_MODIFIER});
        _keyword = keyword;
        _count = count;
    }

    public KeywordModifier(PhysicalCard physicalCard, Filter affectFilter, Condition condition, Keyword keyword, int count) {
        super(physicalCard, "Has " + keyword.getHumanReadable() + ((count > 1) ? (" +" + count) : ""), affectFilter, condition, new ModifierEffect[]{ModifierEffect.KEYWORD_MODIFIER});
        _keyword = keyword;
        _count = count;
    }

    @Override
    public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
        return (result || keyword == _keyword);
    }

    @Override
    public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result) {
        if (keyword == _keyword)
            return result + _count;
        else
            return result;
    }
}
