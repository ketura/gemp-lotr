package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class HealResult extends EffectResult {
    private final PhysicalCard _healedCard;
    private final PhysicalCard _source;
    private final String _performingPlayer;

    public HealResult(PhysicalCard healedCard, PhysicalCard source, String performingPlayer) {
        super(Type.FOR_EACH_HEALED);
        _healedCard = healedCard;
        _source = source;
        _performingPlayer = performingPlayer;
    }

    public PhysicalCard getHealedCard() {
        return _healedCard;
    }
    public PhysicalCard getSource() {
        return _source;
    }
    public String getPerformingPlayer() { return _performingPlayer; }
}
