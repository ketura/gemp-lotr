package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.lotronly.LotroGameUtils;
import com.gempukku.lotro.game.actions.Action;

public class SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier extends AbstractModifier {
    private final Side _side;
    private final Phase _phase;

    public SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(PhysicalCard source, Side side, Phase phase) {
        this(source, null, side, phase);
    }

    public SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(PhysicalCard source, Condition condition, Side side, Phase phase) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _side = side;
        _phase = phase;
    }

    @Override
    public boolean canPlayAction(DefaultGame game, String performingPlayer, Action action) {
        if (action.getActionTimeword() == _phase
                && (
                (_side == Side.FREE_PEOPLE && LotroGameUtils.isFP(game, action.getPerformingPlayer()))
                        || (_side == Side.SHADOW && LotroGameUtils.isShadow(game, action.getPerformingPlayer()))))
            return false;
        return true;
    }
}