package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.Condition;

public class RequirementCondition implements Condition {
    private final Requirement[] requirements;
    private final DefaultActionContext actionContext;

    public RequirementCondition(Requirement[] requirements, DefaultActionContext actionContext) {
        this.requirements = requirements;
        this.actionContext = actionContext;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        for (Requirement requirement : requirements) {
            if (!requirement.accepts(actionContext))
                return false;
        }

        return true;
    }
}
