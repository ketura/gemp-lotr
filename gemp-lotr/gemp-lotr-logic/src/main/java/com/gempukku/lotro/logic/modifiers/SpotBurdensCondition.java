package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.state.LotroGame;

public class SpotBurdensCondition implements Condition {
    private final int _count;

    public SpotBurdensCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        return game.getGameState().getBurdens() >= _count;
    }
}
