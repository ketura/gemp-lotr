package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.Action;

public class CantPlayCardsModifier extends AbstractModifier {
    private final Filter _filters;

    public CantPlayCardsModifier(PhysicalCard source, Filterable... filters) {
        this(source, null, filters);
    }

    public CantPlayCardsModifier(PhysicalCard source, Condition condition, Filterable... filters) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _filters = Filters.and(filters);
    }

    @Override
    public boolean canPlayCard(DefaultGame game, String performingPlayer, PhysicalCard card) {
        return !_filters.accepts(game, card);
    }

    @Override
    public boolean canPlayAction(DefaultGame game, String performingPlayer, Action action) {
        final PhysicalCard actionSource = action.getActionSource();
        if (actionSource != null)
            if (action.getType() == Action.Type.PLAY_CARD)
                if (_filters.accepts(game, actionSource))
                    return false;
        return true;
    }
}
