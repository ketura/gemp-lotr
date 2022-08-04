package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;

public class MinThreatCondition implements Condition {
    private final int _count;

    public MinThreatCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        return game.getGameState().getThreats() >= _count;
    }
}
