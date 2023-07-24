package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;

public class CancelKeywordBonusTargetModifier extends AbstractModifier implements KeywordAffectingModifier {
    private final Keyword _keyword;
    private final Filter _sourceFilter;

    public CancelKeywordBonusTargetModifier(LotroPhysicalCard source, Keyword keyword, Filterable affectFilter, Filterable sourceFilter) {
        this(source, keyword, null, affectFilter, sourceFilter);
    }

    public CancelKeywordBonusTargetModifier(LotroPhysicalCard source, Keyword keyword, Condition condition, Filterable affectFilter, Filterable sourceFilter) {
        super(source, "Cancel " + keyword.getHumanReadable() + " keyword", affectFilter, condition, ModifierEffect.CANCEL_KEYWORD_BONUS_TARGET_MODIFIER);
        _keyword = keyword;
        _sourceFilter = Filters.and(sourceFilter);
    }

    @Override
    public boolean appliesKeywordModifier(DefaultGame game, LotroPhysicalCard modifierSource, Keyword keyword) {
        if (keyword == _keyword
                && (_sourceFilter == null || (modifierSource != null && _sourceFilter.accepts(game, modifierSource))))
            return false;
        return true;
    }

    @Override
    public Keyword getKeyword() {
        return _keyword;
    }
}