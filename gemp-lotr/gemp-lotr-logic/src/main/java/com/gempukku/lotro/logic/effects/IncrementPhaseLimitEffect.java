package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class IncrementPhaseLimitEffect extends UnrespondableEffect {
    private PhysicalCard card;
    private int limit;
    private Phase phase;
    private String prefix;

    public IncrementPhaseLimitEffect(PhysicalCard card, int limit) {
        this.card = card;
        this.limit = limit;
    }

    public IncrementPhaseLimitEffect(PhysicalCard card, Phase phase, int limit) {
        this.card = card;
        this.limit = limit;
        this.phase = phase;
    }

    public IncrementPhaseLimitEffect(PhysicalCard card, String prefix, int limit) {
        this.card = card;
        this.limit = limit;
        this.prefix = prefix;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        if (phase == null)
            game.getGameState().getCurrentPhase();
        game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(card, prefix, phase).incrementToLimit(limit, 1);
    }
}
