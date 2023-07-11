package com.gempukku.lotro.game.modifiers.condition;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.Condition;

public class PhaseCondition implements Condition {
    private final Phase _phase;

    public PhaseCondition(Phase phase) {
        _phase = phase;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return _phase == null || game.getGameState().getCurrentPhase() == _phase;
    }
}
