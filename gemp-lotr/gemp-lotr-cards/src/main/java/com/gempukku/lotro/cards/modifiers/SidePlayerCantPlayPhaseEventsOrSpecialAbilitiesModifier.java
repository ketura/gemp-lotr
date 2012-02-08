package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

public class SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier extends AbstractModifier {
    private Side _side;
    private Phase _phase;

    public SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(PhysicalCard source, Side side, Phase phase) {
        this(source, null, side, phase);
    }

    public SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(PhysicalCard source, Condition condition, Side side, Phase phase) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _side = side;
        _phase = phase;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
        if (action.getActionTimeword() == _phase
                && (
                (_side == Side.FREE_PEOPLE && action.getPerformingPlayer().equals(gameState.getCurrentPlayerId()))
                        || (_side == Side.SHADOW && !action.getPerformingPlayer().equals(gameState.getCurrentPlayerId()))))
            return false;
        return true;
    }
}