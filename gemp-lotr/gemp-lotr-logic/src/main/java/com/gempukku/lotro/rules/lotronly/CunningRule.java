package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.modifiers.ModifiersLogic;
import com.gempukku.lotro.modifiers.condition.PhaseCondition;

public class CunningRule {
    private final ModifiersLogic _modifiersLogic;

    public CunningRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        _modifiersLogic.addAlwaysOnModifier(
                new CantTakeWoundsModifier(null, new PhaseCondition(Phase.ARCHERY), Keyword.CUNNING));
        _modifiersLogic.addAlwaysOnModifier(
                new CantBeAssignedToSkirmishModifier(null, Keyword.CUNNING));
    }
}
