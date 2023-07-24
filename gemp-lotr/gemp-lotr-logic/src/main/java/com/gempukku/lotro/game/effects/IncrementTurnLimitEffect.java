package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class IncrementTurnLimitEffect extends UnrespondableEffect {
    private final LotroPhysicalCard card;
    private final int limit;

    public IncrementTurnLimitEffect(LotroPhysicalCard card, int limit) {
        this.card = card;
        this.limit = limit;
    }

    @Override
    protected void doPlayEffect(DefaultGame game) {
        game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(card).incrementToLimit(limit, 1);
    }
}
