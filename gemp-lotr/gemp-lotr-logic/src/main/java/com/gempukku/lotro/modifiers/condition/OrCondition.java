package com.gempukku.lotro.modifiers.condition;

import com.gempukku.lotro.game.DefaultGame;

public class OrCondition implements Condition {
    private final Condition[] _conditions;

    public OrCondition(Condition... conditions) {
        _conditions = conditions;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        for (Condition condition : _conditions) {
            if (condition != null && condition.isFullfilled(game))
                return true;
        }

        return false;
    }
}
