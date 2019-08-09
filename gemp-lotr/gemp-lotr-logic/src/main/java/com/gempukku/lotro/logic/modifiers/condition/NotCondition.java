package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;

public class NotCondition implements Condition {
    private Condition _condition;

    public NotCondition(Condition condition) {
        _condition = condition;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        return !_condition.isFullfilled(game);
    }
}
