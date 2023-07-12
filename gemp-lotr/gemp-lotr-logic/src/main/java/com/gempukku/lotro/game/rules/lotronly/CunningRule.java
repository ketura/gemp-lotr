package com.gempukku.lotro.game.rules.lotronly;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.game.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.game.modifiers.ModifiersLogic;
import com.gempukku.lotro.game.modifiers.condition.PhaseCondition;

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
