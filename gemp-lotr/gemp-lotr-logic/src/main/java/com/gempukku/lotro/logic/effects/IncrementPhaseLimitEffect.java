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
        this(card, null, "", 1);
    }

    public IncrementPhaseLimitEffect(PhysicalCard card, Phase phase, int limit) {
        this(card, phase, "", 1);
    }

    public IncrementPhaseLimitEffect(PhysicalCard card, String prefix, int limit) {
        this(card, null, "", 1);
    }

    private IncrementPhaseLimitEffect(PhysicalCard card, Phase phase, String prefix, int limit) {
        this.card = card;
        this.phase = phase;
        this.prefix = prefix;
        this.limit = limit;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        if (phase == null)
            phase = game.getGameState().getCurrentPhase();
        game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(card, prefix, phase).incrementToLimit(limit, 1);
    }
}
