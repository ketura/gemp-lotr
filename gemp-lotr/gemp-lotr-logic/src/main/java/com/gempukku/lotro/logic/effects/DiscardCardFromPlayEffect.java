package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;

public class DiscardCardFromPlayEffect extends DiscardCardsFromPlayEffect {
    public DiscardCardFromPlayEffect(PhysicalCard source, PhysicalCard card) {
        super(source, Filters.sameCard(card));
    }
}
