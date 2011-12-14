package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.logic.timing.EffectResult;

public class AfterAllSkirmishesResult extends EffectResult {
    private boolean _createAnExtraAssignmentAndSkirmishPhases;

    public AfterAllSkirmishesResult() {
        super(EffectResult.Type.AFTER_ALL_SKIRMISHES);
    }

    public boolean isCreateAnExtraAssignmentAndSkirmishPhases() {
        return _createAnExtraAssignmentAndSkirmishPhases;
    }

    public void setCreateAnExtraAssignmentAndSkirmishPhases(boolean createAnExtraAssignmentAndSkirmishPhases) {
        _createAnExtraAssignmentAndSkirmishPhases = createAnExtraAssignmentAndSkirmishPhases;
    }
}
