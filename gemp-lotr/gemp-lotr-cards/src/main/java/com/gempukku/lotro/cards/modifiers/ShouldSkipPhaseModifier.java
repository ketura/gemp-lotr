package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ShouldSkipPhaseModifier extends AbstractModifier {
    private Condition _condition;
    private Phase _phase;
    private String _playerId;

    public ShouldSkipPhaseModifier(PhysicalCard source, Phase phase) {
        super(source, "Skip " + phase.toString() + " phase", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER});
        _phase = phase;
    }

    public ShouldSkipPhaseModifier(PhysicalCard source, Condition condition, Phase phase) {
        super(source, "Skip " + phase.toString() + " phase", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER});
        _condition = condition;
        _phase = phase;
    }

    @Override
    public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId) {
        if (phase == _phase && (_playerId == null || _playerId.equals(playerId)) && (_condition == null || _condition.isFullfilled(gameState, modifiersQuerying)))
            return true;
        return false;
    }
}