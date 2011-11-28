package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

public class PlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier extends AbstractModifier {

    private Condition _condition;
    private String _playerId;
    private Phase _phase;

    public PlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(PhysicalCard source, String playerId, Phase phase) {
        this(source, null, playerId, phase);
    }

    public PlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(PhysicalCard source, Condition condition, String playerId, Phase phase) {
        super(source, null, null, ModifierEffect.ACTION_MODIFIER);
        _condition = condition;
        _playerId = playerId;
        _phase = phase;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
        if (_condition == null || _condition.isFullfilled(gameState, modifiersQuerying))
            if (performingPlayer.equals(_playerId) && action.getActionTimeword() == _phase)
                return false;
        return true;
    }
}
