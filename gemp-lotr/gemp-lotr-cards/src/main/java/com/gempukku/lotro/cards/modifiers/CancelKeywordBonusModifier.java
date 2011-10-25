package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CancelKeywordBonusModifier extends AbstractModifier {
    private Keyword _keyword;

    public CancelKeywordBonusModifier(PhysicalCard source, Keyword keyword, Filter affectFilter) {
        super(source, "Cancel " + keyword.getHumanReadable() + " keyword", affectFilter, ModifierEffect.KEYWORD_MODIFIER);
        _keyword = keyword;
    }

    @Override
    public boolean appliesKeywordModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, Keyword keyword) {
        if (keyword == _keyword)
            return false;
        return true;
    }
}