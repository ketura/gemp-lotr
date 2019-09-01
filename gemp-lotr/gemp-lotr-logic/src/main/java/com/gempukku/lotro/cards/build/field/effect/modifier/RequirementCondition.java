package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;

public class RequirementCondition implements Condition {
    private Requirement[] requirements;
    private ActionContext actionContext;

    public RequirementCondition(Requirement[] requirements, ActionContext actionContext) {
        this.requirements = requirements;
        this.actionContext = actionContext;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        for (Requirement requirement : requirements) {
            if (!requirement.accepts(actionContext))
                return false;
        }

        return true;
    }
}
