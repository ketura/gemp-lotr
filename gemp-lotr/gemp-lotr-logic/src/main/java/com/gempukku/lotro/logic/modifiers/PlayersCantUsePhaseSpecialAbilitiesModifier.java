package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

public class PlayersCantUsePhaseSpecialAbilitiesModifier extends AbstractModifier {
    private Phase _phase;

    public PlayersCantUsePhaseSpecialAbilitiesModifier(PhysicalCard source, Phase phase) {
        this(source, null, phase);
    }

    public PlayersCantUsePhaseSpecialAbilitiesModifier(PhysicalCard source, Condition condition, Phase phase) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _phase = phase;
    }

    @Override
    public boolean canPlayAction(LotroGame game, String performingPlayer, Action action) {
        if (action.getActionTimeword() == _phase && action.getType() == Action.Type.SPECIAL_ABILITY)
            return false;
        return true;
    }
}
