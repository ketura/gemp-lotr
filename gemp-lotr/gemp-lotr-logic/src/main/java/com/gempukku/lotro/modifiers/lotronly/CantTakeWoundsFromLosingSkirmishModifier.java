package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;

import java.util.Set;

public class CantTakeWoundsFromLosingSkirmishModifier extends AbstractModifier {
    private final Filter _winnersFilter;

    public CantTakeWoundsFromLosingSkirmishModifier(LotroPhysicalCard source, Filterable affectFilter, Filterable winnersFilter) {
        super(source, "Can't take wounds", affectFilter, ModifierEffect.WOUND_MODIFIER);
        _winnersFilter = Filters.and(winnersFilter);
    }

    @Override
    public boolean canTakeWoundsFromLosingSkirmish(DefaultGame game, LotroPhysicalCard physicalCard, Set<LotroPhysicalCard> winners) {
        if (_winnersFilter == null
                || Filters.filter(winners, game, _winnersFilter).size() > 0)
            return false;
        return true;
    }
}
