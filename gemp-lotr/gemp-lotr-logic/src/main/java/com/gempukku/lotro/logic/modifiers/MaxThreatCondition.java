package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.state.LotroGame;

public class MaxThreatCondition implements Condition {
    private final int _count;

    public MaxThreatCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        return game.getGameState().getThreats() <= _count;
    }
}
