package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

public class PlayersCantPlayPhaseEventsOrSpecialAbilitiesModifier extends AbstractModifier {
    private Condition _condition;
    private Phase _phase;

    public PlayersCantPlayPhaseEventsOrSpecialAbilitiesModifier(PhysicalCard source, Phase phase) {
        this(source, null, phase);
    }

    public PlayersCantPlayPhaseEventsOrSpecialAbilitiesModifier(PhysicalCard source, Condition condition, Phase phase) {
        super(source, null, null, ModifierEffect.ACTION_MODIFIER);
        _condition = condition;
        _phase = phase;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
        if (_condition == null || _condition.isFullfilled(gameState, modifiersQuerying))
            if (action.getActionTimeword() == _phase)
                return false;
        return true;
    }
}
