package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

public class PlayerCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier extends AbstractModifier {
    private final String _playerId;
    private final Phase _phase;

    public PlayerCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier(PhysicalCard source, String playerId, Phase phase) {
        this(source, null, playerId, phase);
    }

    public PlayerCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier(PhysicalCard source, Condition condition, String playerId, Phase phase) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _playerId = playerId;
        _phase = phase;
    }

    @Override
    public boolean canPlayAction(LotroGame game, String performingPlayer, Action action) {
        if ((action.getType() == Action.Type.PLAY_CARD || action.getType() == Action.Type.SPECIAL_ABILITY)
                && performingPlayer.equals(_playerId) && action.getActionTimeword() == _phase)
            return false;
        return true;
    }
}
