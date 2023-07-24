package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class HealResult extends EffectResult {
    private final LotroPhysicalCard _healedCard;
    private final LotroPhysicalCard _source;
    private final String _performingPlayer;

    public HealResult(LotroPhysicalCard healedCard, LotroPhysicalCard source, String performingPlayer) {
        super(Type.FOR_EACH_HEALED);
        _healedCard = healedCard;
        _source = source;
        _performingPlayer = performingPlayer;
    }

    public LotroPhysicalCard getHealedCard() {
        return _healedCard;
    }
    public LotroPhysicalCard getSource() {
        return _source;
    }
    public String getPerformingPlayer() { return _performingPlayer; }
}
