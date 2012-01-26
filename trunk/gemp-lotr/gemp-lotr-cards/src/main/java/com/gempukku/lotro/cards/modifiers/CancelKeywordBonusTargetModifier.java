package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.KeywordAffectingModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CancelKeywordBonusTargetModifier extends AbstractModifier implements KeywordAffectingModifier {
    private Keyword _keyword;
    private Filterable _sourceFilter;

    public CancelKeywordBonusTargetModifier(PhysicalCard source, Keyword keyword, Filterable affectFilter, Filterable sourceFilter) {
        super(source, "Cancel " + keyword.getHumanReadable() + " keyword", affectFilter, ModifierEffect.CANCEL_KEYWORD_BONUS_TARGET_MODIFIER);
        _keyword = keyword;
        _sourceFilter = sourceFilter;
    }

    @Override
    public boolean appliesKeywordModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, Keyword keyword) {
        if (keyword == _keyword
                && (_sourceFilter == null || (modifierSource != null && Filters.and(_sourceFilter).accepts(gameState, modifiersQuerying, modifierSource))))
            return false;
        return true;
    }

    @Override
    public Keyword getKeyword() {
        return _keyword;
    }
}