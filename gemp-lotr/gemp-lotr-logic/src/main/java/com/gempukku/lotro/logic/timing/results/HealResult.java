package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class HealResult extends EffectResult {
    private PhysicalCard _healedCard;
    private PhysicalCard _source;
    private String _performingPlayer;

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
