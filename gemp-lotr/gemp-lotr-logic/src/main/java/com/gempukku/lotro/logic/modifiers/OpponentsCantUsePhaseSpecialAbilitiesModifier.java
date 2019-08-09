package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

public class OpponentsCantUsePhaseSpecialAbilitiesModifier extends AbstractModifier {
    private Phase _phase;
    private String _playerId;

    public OpponentsCantUsePhaseSpecialAbilitiesModifier(PhysicalCard source, Phase phase, String playerId) {
        this(source, null, phase, playerId);
    }

    public OpponentsCantUsePhaseSpecialAbilitiesModifier(PhysicalCard source, Condition condition, Phase phase, String playerId) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _phase = phase;
        _playerId = playerId;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
        if (!performingPlayer.equals(_playerId) && action.getActionTimeword() == _phase && action.getType() == Action.Type.SPECIAL_ABILITY)
            return false;
        return true;
    }
}
