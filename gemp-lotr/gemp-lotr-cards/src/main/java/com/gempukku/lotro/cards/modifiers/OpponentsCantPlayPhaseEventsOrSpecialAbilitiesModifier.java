package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

public class OpponentsCantPlayPhaseEventsOrSpecialAbilitiesModifier extends AbstractModifier {
    private String _playerId;
    private Phase _phase;

    public OpponentsCantPlayPhaseEventsOrSpecialAbilitiesModifier(PhysicalCard source, String playerId, Phase phase) {
        this(source, null, playerId, phase);
    }

    public OpponentsCantPlayPhaseEventsOrSpecialAbilitiesModifier(PhysicalCard source, Condition condition, String playerId, Phase phase) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _playerId = playerId;
        _phase = phase;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
        if (action.getActionTimeword() == _phase && !action.getPerformingPlayer().equals(_playerId))
            return false;
        return true;
    }
}
