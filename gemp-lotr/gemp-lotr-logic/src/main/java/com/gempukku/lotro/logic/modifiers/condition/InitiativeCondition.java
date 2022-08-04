package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;

public class InitiativeCondition implements Condition {
    private final Side _side;

    public InitiativeCondition(Side side) {
        _side = side;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        return game.getModifiersQuerying().hasInitiative(game) == _side;
    }
}
