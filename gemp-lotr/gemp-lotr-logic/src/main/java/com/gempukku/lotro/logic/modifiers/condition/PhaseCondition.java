package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;

public class PhaseCondition implements Condition {
    private Phase _phase;

    public PhaseCondition(Phase phase) {
        _phase = phase;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        return _phase == null || game.getGameState().getCurrentPhase() == _phase;
    }
}
