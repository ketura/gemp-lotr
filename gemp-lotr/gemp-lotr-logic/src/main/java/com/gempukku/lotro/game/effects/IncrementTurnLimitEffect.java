package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class IncrementTurnLimitEffect extends UnrespondableEffect {
    private final PhysicalCard card;
    private final int limit;

    public IncrementTurnLimitEffect(PhysicalCard card, int limit) {
        this.card = card;
        this.limit = limit;
    }

    @Override
    protected void doPlayEffect(DefaultGame game) {
        game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(card).incrementToLimit(limit, 1);
    }
}
