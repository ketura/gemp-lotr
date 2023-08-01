package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.Condition;

public class CantDiscardFromPlayModifier extends AbstractModifier {
    private final Filter _sourceFilter;

    public CantDiscardFromPlayModifier(LotroPhysicalCard source, String text, Filterable affectFilter, Filterable sourceFilter) {
        this(source, text, null, affectFilter, sourceFilter);
    }

    public CantDiscardFromPlayModifier(LotroPhysicalCard source, String text, Condition condition, Filterable affectFilter, Filterable sourceFilter) {
        super(source, text, affectFilter, condition, ModifierEffect.DISCARD_FROM_PLAY_MODIFIER);
        _sourceFilter = Filters.and(sourceFilter);
    }

    @Override
    public boolean canBeDiscardedFromPlay(DefaultGame game, String performingPlayer, LotroPhysicalCard card, LotroPhysicalCard source) {
        if (_sourceFilter.accepts(game, source))
            return false;
        return true;
    }
}
