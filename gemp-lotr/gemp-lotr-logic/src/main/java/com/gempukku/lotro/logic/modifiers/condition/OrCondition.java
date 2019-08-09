package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;

public class OrCondition implements Condition {
    private Condition[] _conditions;

    public OrCondition(Condition... conditions) {
        _conditions = conditions;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        for (Condition condition : _conditions) {
            if (condition != null && condition.isFullfilled(game))
                return true;
        }

        return false;
    }
}
