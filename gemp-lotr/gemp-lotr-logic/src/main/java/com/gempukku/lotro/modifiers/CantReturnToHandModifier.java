package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;

public class CantReturnToHandModifier extends AbstractModifier {
    private final Filter _sourceFilter;

    public CantReturnToHandModifier(LotroPhysicalCard source, String text, Filterable affectFilter, Filterable sourceFilter) {
        super(source, text, affectFilter, ModifierEffect.RETURN_TO_HAND_MODIFIER);
        _sourceFilter = Filters.and(sourceFilter);
    }

    @Override
    public boolean canBeReturnedToHand(DefaultGame game, LotroPhysicalCard card, LotroPhysicalCard source) {
        if (_sourceFilter.accepts(game, source))
            return false;
        return true;
    }
}