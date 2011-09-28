package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ShouldSkipPhaseModifier extends AbstractModifier {
    private Phase _phase;
    private String _playerId;

    public ShouldSkipPhaseModifier(PhysicalCard source, Phase phase) {
        super(source, "Skip " + phase.toString() + " phase", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER});
        _phase = phase;
    }

    @Override
    public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId, boolean result) {
        if (phase == _phase && (_playerId == null || _playerId.equals(playerId)))
            return true;
        return result;
    }
}