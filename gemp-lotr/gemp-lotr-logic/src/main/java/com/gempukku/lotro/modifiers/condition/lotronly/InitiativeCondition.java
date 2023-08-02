package com.gempukku.lotro.modifiers.condition.lotronly;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.Condition;

public class InitiativeCondition implements Condition {
    private final Side _side;

    public InitiativeCondition(Side side) {
        _side = side;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return game.getModifiersQuerying().hasInitiative(game) == _side;
    }
}
