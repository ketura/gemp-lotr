package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
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
    public boolean canPlayAction(LotroGame game, String performingPlayer, Action action) {
        if (action.getActionTimeword() == _phase
                && (
                (_side == Side.FREE_PEOPLE && GameUtils.isFP(game, action.getPerformingPlayer()))
                        || (_side == Side.SHADOW && GameUtils.isShadow(game, action.getPerformingPlayer()))))
            return false;
        return true;
    }
}