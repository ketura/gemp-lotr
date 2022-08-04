package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class ShouldSkipPhaseModifier extends AbstractModifier {
    private final Phase _phase;
    private final String _playerId;

    public ShouldSkipPhaseModifier(PhysicalCard source, Phase phase) {
        this(source, null, phase);
    }

    public ShouldSkipPhaseModifier(PhysicalCard source, Condition condition, Phase phase) {
        this(source, null, condition, phase);
    }

    public ShouldSkipPhaseModifier(PhysicalCard source, String playerId, Condition condition, Phase phase) {
        super(source, "Skip " + phase.toString() + " phase", null, condition, ModifierEffect.ACTION_MODIFIER);
        _playerId = playerId;
        _phase = phase;
    }

    @Override
    public boolean shouldSkipPhase(LotroGame game, Phase phase, String playerId) {
        if (phase == _phase && (_playerId == null || _playerId.equals(playerId)))
            return true;
        return false;
    }
}