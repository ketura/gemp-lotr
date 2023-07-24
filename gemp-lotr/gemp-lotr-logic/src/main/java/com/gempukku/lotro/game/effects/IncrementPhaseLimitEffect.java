package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;

public class IncrementPhaseLimitEffect extends UnrespondableEffect {
    private final LotroPhysicalCard card;
    private final int limit;
    private Phase phase;
    private final String prefix;

    public IncrementPhaseLimitEffect(LotroPhysicalCard card, int limit) {
        this(card, null, "", limit);
    }

    public IncrementPhaseLimitEffect(LotroPhysicalCard card, Phase phase, int limit) {
        this(card, phase, "", limit);
    }

    public IncrementPhaseLimitEffect(LotroPhysicalCard card, String prefix, int limit) {
        this(card, null, prefix, limit);
    }

    private IncrementPhaseLimitEffect(LotroPhysicalCard card, Phase phase, String prefix, int limit) {
        this.card = card;
        this.phase = phase;
        this.prefix = prefix;
        this.limit = limit;
    }

    @Override
    protected void doPlayEffect(DefaultGame game) {
        if (phase == null)
            phase = game.getGameState().getCurrentPhase();
        game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(card, prefix, phase).incrementToLimit(limit, 1);
    }
}
