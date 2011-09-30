package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.timing.ChooseableEffect;

import java.util.Collection;

public class ChooseAndReturnCardsToHandEffect extends ChooseActiveCardsEffect implements ChooseableEffect {
    private CostToEffectAction _action;

    public ChooseAndReturnCardsToHandEffect(CostToEffectAction action, String playerId, int minimum, int maximum, Filter... filters) {
        super(playerId, "Choose cards to return to hand", minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected void cardsSelected(Collection<PhysicalCard> cards) {
        if (_action.getActionSource() != null)
            _action.appendEffect(new CardAffectsCardEffect(_action.getActionSource(), cards));
        _action.appendEffect(new ReturnCardsToHandEffect(_action.getActionSource(), cards.toArray(new PhysicalCard[cards.size()])));
    }
}
