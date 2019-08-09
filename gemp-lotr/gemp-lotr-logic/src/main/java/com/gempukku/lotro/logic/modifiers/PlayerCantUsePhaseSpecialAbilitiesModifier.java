package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

public class PlayerCantUsePhaseSpecialAbilitiesModifier extends AbstractModifier {
    private String _playerId;
    private Phase _phase;

    public PlayerCantUsePhaseSpecialAbilitiesModifier(PhysicalCard source, String playerId, Phase phase) {
        this(source, null, playerId, phase);
    }

    public PlayerCantUsePhaseSpecialAbilitiesModifier(PhysicalCard source, Condition condition, String playerId, Phase phase) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _playerId = playerId;
        _phase = phase;
    }

    @Override
    public boolean canPlayAction(LotroGame game, String performingPlayer, Action action) {
        if (performingPlayer.equals(_playerId) && action.getActionTimeword() == _phase && action.getType() == Action.Type.SPECIAL_ABILITY)
            return false;
        return true;
    }
}
